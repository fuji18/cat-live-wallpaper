# TICKET-024: CI 品質ゲート整備

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-001`, `TICKET-022`, `TICKET-023`

## 目的

ローカル検証コマンドを CI で再現し、最小の品質ゲートを整える。

## スコープ

- `lint`, `testDebugUnitTest`, `assembleDebug` の CI 化
- `.github/workflows/` の Android CI 草案を作る

## 完了条件

- [x] 最低限の検証コマンドを CI で回せる
- [x] main マージ前の品質ゲートが定義されている

## 参照ドキュメント

- [development-guidelines.md](../development-guidelines.md)
- [repository-structure.md](../repository-structure.md)