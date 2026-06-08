# TICKET-014: `drawFrame()` 更新順序の調停実装

- 優先度: `P0`
- ステータス: `todo`
- 依存チケット: `TICKET-004`, `TICKET-010`, `TICKET-012`, `TICKET-013`

## 目的

状態更新から描画までの順序を 1 箇所に固定する。

## スコープ

- draw loop の起点を作る
- 状態更新順序を統一する
- render 呼び出しまでを調停する

## 完了条件

- [ ] 更新順序が一元化されている
- [ ] 後続の Engine 統合で再利用できる

## 参照ドキュメント

- [functional-design.md](../functional-design.md)
- [development-guidelines.md](../development-guidelines.md)