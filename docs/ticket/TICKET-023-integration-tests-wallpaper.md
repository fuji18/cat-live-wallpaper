# TICKET-023: Wallpaper フロー統合テスト実装

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-017`, `TICKET-019`, `TICKET-020`, `TICKET-021`

## 目的

プレビュー起動、オフセット反映、タップ反応を統合テストで検証する。

## スコープ

- Wallpaper 起動経路の検証
- offset 反映の検証
- タップ反応と描画更新の検証

## 完了条件

- [x] MVP の主要統合フローを検証できる
- [x] Engine 統合後の主要 regressions を検知できる

## 参照ドキュメント

- [architecture.md](../architecture.md)
- [development-guidelines.md](../development-guidelines.md)