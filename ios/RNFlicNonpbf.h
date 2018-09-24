#if __has_include(<React/RCTBridgeModule.h>)
  #import <React/RCTBridgeModule.h>
#else
  #import "RCTBridgeModule.h"
#endif

#import <React/RCTEventEmitter.h>
#import <fliclib/fliclib.h>

@interface RNFlicNonpbf : RCTEventEmitter <SCLFlicManagerDelegate, SCLFlicButtonDelegate, RCTBridgeModule>
+ (bool)connectButtonFromUrl:(NSURL *) url;
@end
