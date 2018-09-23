
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif
#import <React/RCTEventEmitter.h>
#import <fliclib/fliclib.h>

@interface RNFlicNonpbf : RCTEventEmitter <SCLFlicManagerDelegate, SCLFlicButtonDelegate, RCTBridgeModule>
+ (bool)connectButtonFromUrl:(NSURL *) url;
@end
