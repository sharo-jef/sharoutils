# Sharo Utilities

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-green.svg)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Fabric-0.18.4-blue.svg)](https://fabricmc.net/)

## 概要

Minecraft Java Edition 1.21.11 向けの便利 Mod

バランス調整のためレシピや機能が変更される可能性あり

[【Minecraft】Sharo Utilities - sharo-jef のブログ](https://sharo-jef.hatenablog.com/entry/2020/12/12/151129)

## 必要環境

- **Minecraft**: 1.21.11
- **Fabric Loader**: 0.18.4 以上
- **Fabric API**: 0.140.2+1.21.11
- **Fabric Language Kotlin**: 1.13.0+kotlin.2.1.0
- **Java**: 21 以上

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
