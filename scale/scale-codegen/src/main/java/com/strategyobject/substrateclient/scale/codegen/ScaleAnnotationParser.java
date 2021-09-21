package com.strategyobject.substrateclient.scale.codegen;

import com.google.common.base.Strings;
import com.strategyobject.substrateclient.common.codegen.AnnotationUtils;
import com.strategyobject.substrateclient.common.codegen.TypeTraverser;
import com.strategyobject.substrateclient.common.utils.StringUtils;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import com.strategyobject.substrateclient.scale.annotations.ScaleGeneric;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ScaleAnnotationParser {
    private final ProcessorContext context;

    public ScaleAnnotationParser(ProcessorContext context) {
        this.context = context;
    }

    public TypeTraverser.TypeTreeNode parse(@NonNull VariableElement field) {
        val scaleType = AnnotationUtils.<TypeMirror>getValueFromAnnotation(field, Scale.class, "value");
        if (scaleType != null) {
            return new TypeTraverser.TypeTreeNode(scaleType);
        }

        val scaleGeneric = AnnotationUtils.getAnnotationMirror(field, ScaleGeneric.class);
        if (scaleGeneric != null) {
            val template = AnnotationUtils.<String>getValueFromAnnotation(scaleGeneric, "template");
            val typesMap = getTypesMap(scaleGeneric);
            return parseTemplate(template, typesMap);
        }

        return null;
    }

    private TypeTraverser.TypeTreeNode parseTemplate(String template, Map<String, TypeMirror> typesMap) {
        val indexes = StringUtils.allIndexesOfAny(template, "<,>");
        if (indexes.size() == 0 || indexes.get(0) == 0) {
            throw new IllegalArgumentException("Wrong template");
        }

        val firstIndex = indexes.get(0);
        val rootType = getMappedType(typesMap, template.substring(0, firstIndex).trim());
        val root = new TypeTraverser.TypeTreeNode(rootType);
        var node = root;
        var nameStart = firstIndex + 1;
        try {
            for (int i = 0; i < indexes.size(); i++) {
                val index = indexes.get(i);
                val op = template.charAt(index);
                if (op == '>') {
                    node = node.getParent();
                    nameStart = index + 1;
                    continue;
                }

                val nameEnd = i < indexes.size() - 1 ? indexes.get(i + 1) : template.length();
                val type = getMappedType(typesMap, template.substring(nameStart, nameEnd).trim());
                val newNode = new TypeTraverser.TypeTreeNode(type);

                if (template.charAt(index) == '<') {
                    node.add(newNode);
                } else if (template.charAt(index) == ',') {
                    node.getParent().add(newNode);
                }

                node = newNode;
                nameStart = nameEnd + 1;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong template", e);
        }

        if (root != node) {
            throw new IllegalArgumentException("Wrong template");
        }

        return root;
    }

    private TypeMirror getMappedType(Map<String, TypeMirror> typesMap, String name) {
        val type = typesMap.get(name);
        return type == null || context.isSubtypeOfScaleAnnotationsDefault(type) ? null : type;
    }

    private Map<String, TypeMirror> getTypesMap(AnnotationMirror scaleGeneric) {
        val annotations = AnnotationUtils.<List<AnnotationMirror>>getValueFromAnnotation(scaleGeneric, "types");
        val result = new HashMap<String, TypeMirror>(Objects.requireNonNull(annotations).size());
        for (val annotation : annotations) {
            var type = AnnotationUtils.<TypeMirror>getValueFromAnnotation(annotation, "value");
            var name = AnnotationUtils.<String>getValueFromAnnotation(annotation, "name");
            validateScaleAnnotationIsNotEmpty(name, type);

            if (type == null) {
                type = context.getScaleAnnotationsDefaultType();
            }

            if (Strings.isNullOrEmpty(name)) {
                name = ((DeclaredType) type).asElement().getSimpleName().toString();
            }

            result.put(name, type);
        }

        return result;
    }

    private void validateScaleAnnotationIsNotEmpty(String name, TypeMirror type) {
        if (Strings.isNullOrEmpty(name) && type == null) {
            throw new IllegalArgumentException("Empty Scale annotation.");
        }
    }
}
