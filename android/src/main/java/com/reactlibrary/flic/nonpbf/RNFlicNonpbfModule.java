
package com.reactlibrary.flic.nonpbf;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;

// import io.flic.lib.FlicButton;
// import io.flic.lib.FlicButtonCallback;
// import io.flic.lib.FlicButtonCallbackFlags;
// import io.flic.lib.FlicManager;
// import io.flic.lib.FlicManagerInitializedCallback;

public class RNFlicNonpbfModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  private static final String TAG = "FLIC";
  private static final String EVENT_NAMESPACE = "FLIC";
  private static final String APP_ID = "";
  private static final String APP_SECRET = "";
  private static final String APP_URL = "FlicExampleApp";

  public RNFlicNonpbfModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

    // FlicManager.setAppCredentials(APP_ID, APP_SECRET, APP_URL);
  }

  @Override
  public String getName() {
    return "RNFlicNonpbf";
  }

//   @ReactMethod
//   public void grabFlicButton(final Promise promise) {
//     try {
//       FlicManager.getInstance(this, new FlicManagerInitializedCallback() {
//         @Override
//         public void onInitialized(FlicManager manager) {
//           manager.initiateGrabButton(this.reactContext);
//         }
//       });
//     } catch (FlicAppNotInstalledException err) {
//       Log.i(TAG, "FlicAppNotInstalledException");
//     }
//     promise.resolve("grabFlicButton");
//   }

//   @Override
//   public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
//     FlicManager.getInstance(this, new FlicManagerInitializedCallback() {
//       @Override
//       public void onInitialized(FlicManager manager) {
//         FlicButton button = manager.completeGrabButton(requestCode, resultCode, data);
//         if (button != null) {
//           button.registerListenForBroadcast(FlicBroadcastReceiverFlags.UP_OR_DOWN | FlicBroadcastReceiverFlags.REMOVED);
//           Log.i(TAG, "Grabbed a button");
//         } else {
//           Log.i(TAG, "Did not grab any button");
//         }
//       }
//     });
//   }
// }

// public class FlicNonpbfBroadcastReceiver extends FlicBroadcastReceiver {

//   private static final String TAG = "FLIC";
//   private static final String EVENT_NAMESPACE = "FLIC";
//   private static final String APP_ID = "";
//   private static final String APP_SECRET = "";
//   private static final String APP_URL = "FlicExampleApp";

//   @Override
//   protected void onRequestAppCredentials(Context context) {
//     FlicManager.setAppCredentials(APP_ID, APP_SECRET, APP_URL);
//   }
  
//   @Override
//   public void onButtonUpOrDown(Context context, FlicButton button, boolean wasQueued, int timeDiff, boolean isUp, boolean isDown) {
//     if (isUp) {
//       Log.i(TAG, "Button clicked up");
//     } else {
//       Log.i(TAG, "Button clicked down");
//     }
//   }
  
//   @Override
//   public void onButtonRemoved(Context context, FlicButton button) {
//     Log.i(TAG, "Button removed");
//   }
}