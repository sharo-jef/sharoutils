<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

# Sharo Utilities

## 概要

Minecraft JE 1.16.4 向けの便利 Mod

バランス調整のためレシピや機能が変更される可能性あり

[【Minecraft】Sharo Utilities - sharo-jef のブログ](https://sharo-jef.hatenablog.com/entry/2020/12/12/151129)

## 設定

この Mod は `config/sharoutils.json` に設定ファイルを生成します。
初回起動時に自動的にデフォルト値で作成されます。

### 設定項目

```json
{
  // Playfulインタラクションの発動確率 (0.0001 = 0.01%/tick)
  "playfulTriggerChance": 0.0001,

  // Sharoエンティティの自然スポーンを有効化
  "sharoNaturalSpawnEnabled": true,

  // Sharoエンティティのスポーン重み (デフォルト: 100)
  "sharoSpawnWeight": 100,

  // Sharoエンティティのスポーンする最小グループサイズ
  "sharoSpawnMinGroupSize": 2,

  // Sharoエンティティのスポーンする最大グループサイズ
  "sharoSpawnMaxGroupSize": 4,

  // Sharo Earthからのモブスポーンを有効化
  "sharoEarthSpawnEnabled": true,

  // Sharo Earthからスポーンする最小数
  "sharoEarthSpawnMinCount": 1,

  // Sharo Earthからスポーンする最大数
  "sharoEarthSpawnMaxCount": 3,

  // Sharo Ringのクラフトレシピを有効化
  "sharoRingCraftingEnabled": true
}
```

設定を変更した後は、サーバー/ワールドを再起動してください。
