# TICKET-001: ルート Gradle と Wrapper の整備

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: なし

## 目的

ルートの Gradle 実行基盤を作り、後続チケットが Android プロジェクトを追加できる状態にする。

## スコープ

- `settings.gradle.kts` を追加する
- ルート `build.gradle.kts` を追加する
- `gradle.properties` と Wrapper を整備する

## 完了条件

- [x] ルート Gradle ファイルが揃っている
- [x] `./gradlew tasks` が実行できる

## 参照ドキュメント

- [repository-structure.md](../repository-structure.md)
- [architecture.md](../architecture.md)