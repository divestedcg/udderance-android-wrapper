![Banner](https://divested.dev/images/featureGraphics/Udderance.png)

Udderance Android Wrapper
=========================

Overview
--------
This is a wrapper allowing for true offline usage of https://udderance.app

Usage
-----
[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/app.udderance/)

Compiling
---------
```
git clone https://github.com/divestedcg/udderance
currentTag=$(git describe --tags --abbrev=0)
cd udderance
git checkout $currentTag
bash prepare-mulberry.sh
bash generate.sh
bash package-android.sh
cd ..
mkdir -v app/src/main/assets
mv -v udderance/output-android/* app/src/main/assets/
gradle assembleRelease
```
