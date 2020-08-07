
package com.reactlibrary.flic.nonpbf;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;

import io.flic.lib.FlicButton;
import io.flic.lib.FlicButtonCallback;
import io.flic.lib.FlicButtonCallbackFlags;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;
import io.flic.lib.FlicAppNotInstalledException;



public class RNFlicNonpbfModule extends ReactContextBaseJavaModule  implements ActivityEventListener{

  private final ReactApplicationContext reactContext;

  private static final String TAG = "FLIC";
  private static final String EVENT_NAMESPACE = "FLIC";
  private static final String APP_ID = "";
  private static final String APP_SECRET = "";
  private static final String APP_URL = "FlicExampleApp";

HashMap<FlicButton, FlicButtonCallback> listeners = new HashMap<>();

   @Override
   public void onActivityResult(Activity activ, final int requestCode, final int resultCode, final Intent data) {
     FlicManager.getInstance(this.reactContext, new FlicManagerInitializedCallback() {
       @Override
       public void onInitialized(FlicManager manager) {
         FlicButton button = manager.completeGrabButton(requestCode, resultCode, data);
         if (button != null) {
           button.registerListenForBroadcast(FlicButtonCallbackFlags.UP_OR_DOWN | FlicButtonCallbackFlags.CLICK_OR_HOLD);
           Log.i(TAG, "Grabbed a button");
         } else {
           Log.i(TAG, "Did not grab any button");
         }
       }
     });
   }

   @Override
  public void onNewIntent(Intent intent) {

  }


   public RNFlicNonpbfModule(ReactApplicationContext reactContext) {
      super(reactContext);
      this.reactContext = reactContext;
      // FlicManager.init(this.reactContext, APP_ID, APP_SECRET);
      this.reactContext.addActivityEventListener(this);

      FlicManager.setAppCredentials(APP_ID, APP_SECRET, APP_URL);
    }


    @Override
    public String getName() {
      return "RNFlicNonpbf";
    }

  private void sendEventMessage(HashMap<String, String> body) {
    WritableMap args = new WritableNativeMap();
    for (Map.Entry<String, String> entry : body.entrySet()) {
        args.putString(entry.getKey(), entry.getValue());
    }

    this.getReactApplicationContext()
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(EVENT_NAMESPACE, args);
  }

  public class FlicButtonListener extends FlicButtonCallback{
      public void onConnect(FlicButton button) {
        HashMap<String, String> data = new HashMap<>();
        data.put("event", "BUTTON_CONNECTED");
        data.put("buttonId", button.getButtonId());
        Log.i(TAG, "onConnect: " + button.getButtonId());
      }
  
      public void onReady(FlicButton button) {
        HashMap<String, String> data = new HashMap<>();
        data.put("event", "BUTTON_READY");
        data.put("buttonId", button.getButtonId());
        Log.i(TAG, "onReady: " + button.getButtonId());
      }
  
      public void onDisconnect(FlicButton button, int flicError, boolean willReconnect) {
        HashMap<String, String> data = new HashMap<>();
        data.put("event", "BUTTON_DISCONNECTED");
        data.put("buttonId", button.getButtonId());
        sendEventMessage(data);
        Log.i(TAG, "onDisconnect: " + button.getButtonId() + ", error: " + flicError + ", willReconnect: " + willReconnect);
      }
  
      public void onConnectionFailed(FlicButton button, int status) {
        HashMap<String, String> data = new HashMap<>();
        data.put("event", "BUTTON_CONNECTING_FAILED");
        data.put("buttonId", button.getButtonId());
        sendEventMessage(data);
        Log.e(TAG, "onConnectionFailed " + button.getButtonId() + ", status " + status);
      }
  
      public void onButtonUpOrDown(final FlicButton button, boolean wasQueued, int timeDiff, final boolean isUp, final boolean isDown) {
        HashMap<String, String> data = new HashMap<>();
        data.put("event", "BUTTON_" + (isDown ? "PRESSED" : "RELEASED"));
        data.put("buttonId", button.getButtonId());
        sendEventMessage(data);
        Log.i(TAG, "Button " + button.getButtonId() + " was " + (isDown ? "PRESSED" : "RELEASED"));
      }
  }

  private void setupEventListenerForButtonInActivity(FlicButton button) {
    FlicButtonListener listener = new FlicButtonListener();

    button.addFlicButtonCallback(listener);
    button.setActiveMode(true);

    // Save the event listener so we can remove it later
    listeners.put(button, listener);
  }

  @ReactMethod
  public void getMessageTest(String name, Callback stringCallback){
    HashMap<String, String> data = new HashMap<>();
    data.put("event", "BUTTON_CONNECTED");
    //data.put("initialized", FlicManager.isInitialized());
    sendEventMessage(data);

    stringCallback.invoke(FlicManager.hasSetAppCredentials());

  }

//NOT TESTED
  @ReactMethod
  public void getKnownButtons(String name, final Promise promise) {
    FlicManager.getInstance(this.reactContext, new FlicManagerInitializedCallback() {
         @Override
         public void onInitialized(FlicManager manager) {
               for (FlicButton button : manager.getKnownButtons() ) {
                  Log.i(TAG, "Connected button " + button.getButtonId());

                  HashMap<String, String> data2 = new HashMap<>();
                  data2.put("event", "BUTTON_CONNECTED");
                  data2.put("buttonId", button.getButtonId());
                  sendEventMessage(data2);
                  
                  setupEventListenerForButtonInActivity(button);
                }

                promise.resolve("GET_KNOWN_BUTTONS");
         }
       });
  }

   @ReactMethod
   public void grabFlicButton(final Promise promise) {
     try {
      final ReactApplicationContext thatContext = this.reactContext;
       FlicManager.getInstance(this.reactContext, new FlicManagerInitializedCallback() {
         @Override
         public void onInitialized(FlicManager manager) {
           manager.initiateGrabButton(thatContext.getCurrentActivity());
         }
       });
     } catch (FlicAppNotInstalledException err) {
       Log.i(TAG, "FlicAppNotInstalledException");
     }
     promise.resolve("grabFlicButton");
   }


//NOT TESTED
  @ReactMethod
  public void makeCall(String number, final Promise promise) {
    String dial = "tel:" + number;

    Intent intent = new Intent(Intent.ACTION_CALL);
    intent.setData(Uri.parse(dial));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    this.getReactApplicationContext().startActivity(intent);

    HashMap<String, String> data = new HashMap<>();
    data.put("event", "MAKING_CALL");
    sendEventMessage(data);
    
    promise.resolve("MAKING_CALL");
  }

  /*
  @ReactMethod
  public void searchButtons(String name, final Promise promise) {
    FlicManager.getInstance(this.reactContext, new FlicManagerInitializedCallback() {
         @Override
         public void onInitialized(FlicManager manager) {
               manager.getScanWizard().start(new FlicScanWizard.Callback() {
                  @Override
                  public void onDiscovered(FlicScanWizard wizard, String bdAddr, int rssi, final boolean isPrivateMode, int revision) {
                    if (isPrivateMode) {
                      HashMap<String, String> data = new HashMap<>();
                      data.put("event", "}");
                      data.put("buttonId", bdAddr);
                      sendEventMessage(data);
                      Log.i(TAG, "Found a private button. Hold it down for 7 seconds to make it public.");
                    } else {
                      HashMap<String, String> data = new HashMap<>();
                      data.put("event", "FOUND_PUBLIC_BUTTON");
                      data.put("buttonId", bdAddr);
                      sendEventMessage(data);
                      Log.i(TAG, "Found a button. Now connecting...");
                    }
                  }
                });
               
             }
           });
    FlicManager.getManager().getScanWizard().start(new FlicScanWizard.Callback() {
      @Override
      public void onDiscovered(FlicScanWizard wizard, String bdAddr, int rssi, final boolean isPrivateMode, int revision) {
        if (isPrivateMode) {
          HashMap<String, String> data = new HashMap<>();
          data.put("event", "}");
          data.put("buttonId", bdAddr);
          sendEventMessage(data);
          Log.i(TAG, "Found a private button. Hold it down for 7 seconds to make it public.");
        } else {
          HashMap<String, String> data = new HashMap<>();
          data.put("event", "FOUND_PUBLIC_BUTTON");
          data.put("buttonId", bdAddr);
          sendEventMessage(data);
          Log.i(TAG, "Found a button. Now connecting...");
        }
      }
  
      @Override
      public void onBLEConnected(FlicScanWizard wizard, String bdAddr) {
        HashMap<String, String> data = new HashMap<>();
        data.put("event", "CONNECTION_ESTABLISHED");
        data.put("buttonId", bdAddr);
        sendEventMessage(data);
        Log.i(TAG, "Connection established. Now verifying...");
      }
  
      @Override
      public void onCompleted(FlicScanWizard wizard, final FlicButton button) {
        HashMap<String, String> data = new HashMap<>();
        data.put("event", "BUTTON_ADDED_SUCCESS");
        data.put("buttonId", button.getButtonId());
        sendEventMessage(data);
        Log.i(TAG, "New button successfully added!");
      }
  
      @Override
      public void onFailed(FlicScanWizard wizard, int flicScanWizardErrorCode) {
        HashMap<String, String> data = new HashMap<>();
        data.put("event", "BUTTON_ADDED_FAILED");
        sendEventMessage(data);
        Log.i(TAG, "Adding a button failed! " + Integer.toString(flicScanWizardErrorCode));
      }
    });

    promise.resolve("ready to scan");
  }
  */

  // @Override
  // public void onHostDestroy() {
  //   // super.onDestroy();

  //   Log.i(TAG, "Destroy FlicModule");

  //  for (Map.Entry<FlicButton, FlicButtonListener> entry : listeners.entrySet()) {
  //     Log.i(TAG, "Remove Flic Event Listener");
  //    entry.getKey().removeEventListener(entry.getValue());
  //    entry.getKey().returnTemporaryMode(FlicButtonMode.SuperActive);
  //  }

  //   FlicManager.getManager().getScanWizard().cancel();
  // }


  

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