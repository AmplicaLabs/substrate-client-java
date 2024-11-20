<a href="https://github.com/w3f/Grants-Program/blob/master/applications/substrate_client_java.md">
  <img align="right" width="450" src="./docs/web3 foundation_grants_badge_black.svg">
</a>

# About

This project provides written in Java API for interaction with a Polkadot or Substrate based network.

## Key features

### Java 8 or higher.

The main idea is to make this library available and usable for as many projects as possible.

### Non-blocking API.

The API is designed to be non-blocking in order to not engage resources for waiting responses from a node, especially when working with web sockets.
Each method returns `CompletableFuture<>`.

### Declarative approach.

Some of our goals are:

- make API simpler for usage;
- let users organize their code similar to the one in the pallet they are interacting with;
- hide difficulties of low level and infrastructure code;
- facilitate the future maintenance.

The best approach to reach project’s goals is to use annotations and code generation techniques. Below are listed some annotations that this API shall provide:

- [x] Scale

  - [x] `@ScaleWriter`;
  - [x] `@ScaleReader`;
  - [x] `@Scale`;
  - [x] `@ScaleGeneric`;
  - [x] `@Ignore`;

- [x] Rpc

  - [x] `@RpcInterface`;
  - [x] `@RpcCall`;
  - [x] `@RpcSubscription`;
  - [x] `@RpcEncoder`;
  - [x] `@RpcDecoder`;

- [ ] Pallet
  - [x] `@Pallet`;
  - [ ] `@Transaction`;
  - [x] `@Storage`;
  - [x] `@Event`.

These allow the generation of scale serializers, deserializers, RPC methods, code for interaction with pallet, etc.
More examples you can find below.

### Deferred parametrization of codecs

Annotations for codecs allow deferring parameters of a generic until it's used at an RPC method. E.g.:

```java
@RequiredArgsConstructor
@Getter
@ScaleWriter
public class Some<A> {
   private final int number;
   private final A some;
}

@RpcInterface(section = "test")
public interface TestSection {
 @RpcCall(method = "sendSome")
 CompletableFuture<Boolean> doNothing(@Scale Some<Parameter> value);
}
```

Annotation processors will generate scale writer for the class `Some` which expects another writer as a dependency.
When a processor faces a parameter like `Some<String> value`, it injects the `Strings`'s writer into the writer of `Some`.

### GC Manageable requests.

We take care of either lost responses or canceled futures by not holding handlers that are needed to match an RPC request with a response.

### Tests run with substrate node.

All API methods related to the substrate node will be tested for operability and compatibility.
Currently, we use [test containers](https://www.testcontainers.org/) and docker image [parity/substrate:v3.0.0](https://hub.docker.com/layers/parity/substrate/v3.0.0/images/sha256-1aef07509d757c584320773c476dcb6077578bbf2f5e468ceb413dcf908897f1?context=explore).

## Building Locally

### Install Prerequisites

1. Install [asdf](https://asdf-vm.com/guide/getting-started.html) tools version manager. Make sure to follow instructions for your shell (e.g. ZSH, Fish, etc.) and package manager (e.g. Homebrew)

   - Install the [Rust plugin](https://github.com/asdf-community/asdf-rust).
   - Install the [Java plugin](https://github.com/halcyon/asdf-java).
   - Then, run `asdf install` at the root of the repo to install tools listed in `/.tool-versions`.
   - Verify your local Java version matches what's in /.tool-versions. If they don't match check the `$PATH` env var to ensure `~/.asdf/shims` directory has precedence over others.
     ```sh
     java -version
     cat .tool-versions
     ```

1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/), an application for managing Docker containers and images along with docker CLI tools.

   _Note: If using fish shell you'd need to update the $PATH manually with this command, so `docker` executables can be found and restart the shell:_

   ```sh
   set -U fish_user_paths /Applications/Docker.app/Contents/Resources/bin $fish_user_paths
   ```

### Build and Publish

1. Build project.

   ```sh
   ./gradlew clean build
   ```

1. Publish compile artifacts to the local Maven cache, so other apps (e.g. saas-frequency-client, Custodial Wallet) can consume it.

   ```sh
   ./gradlew publishToMavenLocal
   ```

### Clean up Built Artifacts

To delete compiled artifacts in local `build` directories:

```sh
./gradlew clean
```

To verify all compiled artifacts have been deleted run this command in the repo root:

```sh
find . -type d -name "build"
---
(should be empty output)
```
