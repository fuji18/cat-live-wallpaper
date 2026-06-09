# TICKET-019: Engine タップ受理と `PLAY` 優先制御実装

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-016`, `TICKET-018`, `TICKET-012`

## 目的

Engine のタップ入力を反応制御へ接続し、通常遷移より `PLAY` を優先させる。

## スコープ

- `onTouchEvent()` を接続する
- `PLAY` 優先フラグまたは同等経路を実装する
- タップ座標のクランプを行う

## 完了条件

- [x] タップ入力から毛糸玉表示へ接続できる（ACTION_UP → touchReactionController.onTap → coordinator.updateState）
- [x] タップ後に `PLAY` を優先できる（ToyState.isVisible=true → CatBehaviorControllerImpl が PLAY を優先、既実装）

## 参照ドキュメント

- [product-requirements.md](../product-requirements.md)
- [functional-design.md](../functional-design.md)