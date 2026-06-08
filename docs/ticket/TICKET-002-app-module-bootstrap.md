# TICKET-002: `app/` モジュールの最小骨格作成

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-001`

## 目的

Android アプリ本体の最小モジュールを追加し、コード配置先を確定する。

## スコープ

- `app/build.gradle.kts` を作成する
- `src/main/`, `src/test/`, `src/androidTest/` の骨格を作る
- package ディレクトリを作成する

## 完了条件

- [x] `app/` モジュールが作成されている
- [x] 想定ディレクトリ構造が `repository-structure.md` と一致する

## 参照ドキュメント

- [repository-structure.md](../repository-structure.md)
- [development-guidelines.md](../development-guidelines.md)