<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

# Kotlin Mod Template

<div class="alert alert-info">
    VSCodeを使用する場合、<a href="https://marketplace.visualstudio.com/items?itemName=aaron-bond.better-comments">Better Comments</a>をインストールすると書き換え位置がわかりやすくなる。
</div>

Minecraft Forge Version: 1.16.4-35.1.7

# 初期化

## build.gradle

1. VERSION
    - コメントアウトのformatに従ってバージョンを変更する。
2. PACKAGE
    - &lt;username&gt;と&lt;modid&gt;を書き換える。 ( `src/main/kotlin/` 内のディレクトリ構成もこの通りにする。)
3. MODID
    - &lt;modid&gt;を書き換える。
4. MODID
    - &lt;modid&gt;を書き換える。
5. USERNAME
    - &lt;username&gt;を書き換える。
6. USERNAME
    - &lt;username&gt;を書き換える。

## src/main/resources/META-INF/mods.toml

<div class="alert alert-warning">
tomlファイルでは<a href="https://marketplace.visualstudio.com/items?itemName=aaron-bond.better-comments">Better Comments</a>のハイライトが効かない。
</div>

7. LICENSE
    - ライセンスを変更する場合は書き換える。
8. MODID
    - &lt;modid&gt;を書き換える
9. MODNAME (USER FRIENDLY)
    - &lt;modname&gt;を書き換える
10. URL (OPTIONAL)
    - リリースページ等のURLに変更する
11. USERNAME (OPTIONAL)
    - &lt;username&gt;を書き換える。
12. DESCRIPTION (OPTIONAL)
    - Modの説明文を書く。

## LICENSE

13. GithubのLICENSE書き換え機能を利用することを推奨する。
