
# react-native-flic-nonpbf

If you are looking for the PBF (Powered by Flic, whitelabel) version look here: https://github.com/mdahiemstra/react-native-flic

This react-native module is for the normal Flic version.

## Getting started

`$ npm install react-native-flic-nonpbf --save`

### Mostly automatic installation

`$ react-native link react-native-flic-nonpbf`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-flic-nonpbf` and add `RNFlicNonpbf.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNFlicNonpbf.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.flic.nonpbf.RNFlicNonpbfPackage;` to the imports at the top of the file
  - Add `new RNFlicNonpbfPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-flic-nonpbf'
  	project(':react-native-flic-nonpbf').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-flic-nonpbf/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-flic-nonpbf')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNFlicNonpbf.sln` in `node_modules/react-native-flic-nonpbf/windows/RNFlicNonpbf.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Flic.Nonpbf.RNFlicNonpbf;` to the usings at the top of the file
  - Add `new RNFlicNonpbfPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import Flic from 'react-native-flic-nonpbf';

// See example app https://github.com/mdahiemstra/react-native-flic-nonpbf-example
```
  