# TICKET-105: 設定値のランタイム反映実装

- 優先度: `P1`
- ステータス: `backlog`
- 依存チケット: `TICKET-015`, `TICKET-103`, `TICKET-104`

## 目的

設定画面で変更した値を壁紙のランタイム挙動へ安全に反映する。

## スコープ

- 設定値読込を service / engine に接続する
- 速度や頻度などの設定を反映する

## 完了条件

- [ ] 設定変更が壁紙挙動へ反映される
- [ ] 設定なしでも既定値で動作する

## 参照ドキュメント

- [product-requirements.md](../product-requirements.md)
- [architecture.md](../architecture.md)