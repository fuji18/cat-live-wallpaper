# TICKET-022: ロジック / オーケストレーションのユニットテスト実装

- 優先度: `P0`
- ステータス: `todo`
- 依存チケット: `TICKET-011`, `TICKET-012`, `TICKET-013`, `TICKET-018`

## 目的

純粋ロジックをユニットテストで固定し、状態遷移と間隔制御の回 regressions を防ぐ。

## スコープ

- behavior のユニットテスト
- frame interval のユニットテスト
- touch reaction のユニットテスト

## 完了条件

- [ ] 主要ロジックのユニットテストがある
- [ ] 時刻や乱数をテストダブルで差し替えられる

## 参照ドキュメント

- [development-guidelines.md](../development-guidelines.md)
- [architecture.md](../architecture.md)