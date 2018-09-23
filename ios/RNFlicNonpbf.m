#import "RNFlicNonpbf.h"
#import <fliclib/fliclib.h>

@implementation RNFlicNonpbf {
    bool hasListeners;
}

+ (BOOL) requiresMainQueueSetup {
    return YES;
}

NSString *eventNamespace = @"FLIC";

- (void) startObserving {
    hasListeners = YES;
}

- (void) stopObserving {
    hasListeners = NO;
}

- (void) sendEventMessage:(NSDictionary *)body {
    NSLog(@"FLICLIB EVENT %@", body);
    
    if (hasListeners) {
        [self sendEventWithName:eventNamespace body: body];
    }
}

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

- (NSArray<NSString *> *)supportedEvents {
    return @[eventNamespace];
}

RCT_EXPORT_METHOD(initFlic:(NSString *)appID withSecret:(NSString *)appSecret) { 
    [SCLFlicManager configureWithDelegate:self defaultButtonDelegate:self appID:appID appSecret:appSecret backgroundExecution:YES];
    [self sendEventMessage: @{@"event": @"initFlicManager"}];
}

RCT_EXPORT_METHOD(grabFlicButton:(NSString *)appUrl) {
    [[SCLFlicManager sharedManager] grabFlicFromFlicAppWithCallbackUrlScheme:appUrl];
    [self sendEventMessage: @{@"event": @"grabFlicFromFlicAppWithCallbackUrlScheme"}];
}

RCT_EXPORT_METHOD(getButtonCount) {
    [self sendEventMessage: @{
        @"event": @"getButtonCount",
        @"buttons": @([SCLFlicManager sharedManager].knownButtons.count)
    }];
}

RCT_EXPORT_METHOD(removeAllButtons) {
    [self sendEventMessage: @{@"event": @"removeAllButtons"}];

    NSDictionary *knownButtons = [SCLFlicManager sharedManager].knownButtons;

    [knownButtons enumerateKeysAndObjectsUsingBlock:^(NSString * key, SCLFlicButton * button, BOOL *stop) {
        [[SCLFlicManager sharedManager] forgetButton:button];
    }];
}

RCT_EXPORT_METHOD(connectKnownButtons) {
    [self sendEventMessage: @{@"event": @"connectKnownButtons"}];

    NSDictionary *knownButtons = [SCLFlicManager sharedManager].knownButtons;

    [knownButtons enumerateKeysAndObjectsUsingBlock:^(NSString * key, SCLFlicButton * button, BOOL *stop) {
        [button connect];
    }];
}

RCT_EXPORT_METHOD(disconnectKnownButtons) {
    [self sendEventMessage: @{@"event": @"disconnectKnownButtons"}];

    NSDictionary *knownButtons = [SCLFlicManager sharedManager].knownButtons;

    [knownButtons enumerateKeysAndObjectsUsingBlock:^(NSString * key, SCLFlicButton * button, BOOL *stop) {
        [button disconnect];
    }];
}

RCT_EXPORT_METHOD(indicateLED:(NSInteger *_Nonnull)count) {
    [self sendEventMessage: @{@"event": @"indicateLED"}];

    NSDictionary *knownButtons = [SCLFlicManager sharedManager].knownButtons;

    [knownButtons enumerateKeysAndObjectsUsingBlock:^(NSString * key, SCLFlicButton * button, BOOL *stop) {
        [button indicateLED:(SCLFlicButtonLEDIndicateCount)count];
    }];
}

RCT_EXPORT_METHOD(getBluetoothState) {
    BOOL flag = [SCLFlicManager sharedManager].enabled;

    [self sendEventMessage: @{
        @"event": @"getBluetoothState",
        @"status": flag ? @"enabled" : @"disabled"
    }];
}

// -- SCLFlicManagerDelegate --

- (void) flicManager:(SCLFlicManager *)manager didGrabFlicButton:(SCLFlicButton *)button withError:(NSError *)error; {
    if (error) {
        [self sendEventMessage: @{
            @"event": @"didGrabFlicButtonError",
            @"error": error
        }];
    } else {
        [self sendEventMessage: @{@"event": @"didGrabFlicButton"}];
    }

    // un-comment the following line if you need lower click latency for your application
    // this will consume more battery so don't over use it
    // button.lowLatency = YES;
}

- (void) flicManagerDidRestoreState:(SCLFlicManager *)manager; {
    [self sendEventMessage: @{@"event": @"flicManagerDidRestoreState"}];
}

// Public method called by RN when app is opened by app URL
// This connects buttons from the URL got from Flic App.
+ (bool) connectButtonFromUrl:(NSURL *)url {
    return [[SCLFlicManager sharedManager] handleOpenURL:url];
}

// -- SCLFlicButtonDelegate --

// DOCS: https://partners.flic.io/partners/developers/documentation/ios/Protocols/SCLFlicButtonDelegate.html

// The flic registered a button down event.
- (void) flicButton:(SCLFlicButton *) button didReceiveButtonDown:(BOOL) queued age: (NSInteger) age; {
    [self sendEventMessage: @{
        @"event": @"didReceiveButtonDown",
        // @"button": button,
        @"queued": @(queued),
        @"age": @(age)
    }];
}

// The flic registered a button up event.
- (void) flicButton:(SCLFlicButton *) button didReceiveButtonUp:(BOOL) queued age: (NSInteger) age; {
    [self sendEventMessage: @{
        @"event": @"didReceiveButtonUp",
        // @"button": button,
        @"queued": @(queued),
        @"age": @(age)
    }];
}

// The flic registered a button click event.
// The behavior of this event depends on what
// "SCLFlicButtonTriggerBehavior" the triggerBehavior property is set to.
- (void) flicButton:(SCLFlicButton *) button didReceiveButtonClick:(BOOL) queued age: (NSInteger) age; {
    [self sendEventMessage: @{
        @"event": @"didReceiveButtonClick",
        // @"button": button,
        @"queued": @(queued),
        @"age": @(age)
    }];
}

// The flic registered a button double click event.
// The behavior of this event depends on what
// "SCLFlicButtonTriggerBehavior" the triggerBehavior property is set to.
- (void) flicButton:(SCLFlicButton *) button didReceiveButtonDoubleClick:(BOOL) queued age: (NSInteger) age; {
    [self sendEventMessage: @{
        @"event": @"didReceiveButtonDoubleClick",
        @"queued": @(queued),
        @"age": @(age)
    }];
}

// The flic registered a button hold event.
// The behavior of this event depends on what
// "SCLFlicButtonTriggerBehavior" the triggerBehavior property is set to.
- (void) flicButton:(SCLFlicButton *) button didReceiveButtonHold:(BOOL) queued age: (NSInteger) age; {
    [self sendEventMessage: @{
        @"event": @"didReceiveButtonHold",
        @"queued": @(queued),
        @"age": @(age)
    }];
}

// This delegate method is called every time the flic
// physically connected to the iOS device, regardless
// of the reason for it. Keep in mind that you also
// have to wait for the flicButtonIsReady: before
// the flic is ready for use.
// The connectionState is not guaranteed to switch
// to SCLFlicButtonConnectionStateConnected until
// after the flicButtonIsReady: callback has arrived.
- (void)flicButtonDidConnect:(SCLFlicButton *_Nonnull)button {
    // NSString *userAssignedName = [SCLFlicButton userAssignedName];
    [self sendEventMessage: @{
        @"event": @"flicButtonDidConnect"
        // @"userAssignedName": @(userAssignedName)
    }];
}

// This delegate method is called every time the flic
// has sucessfully connected and the autheticity has
// been verified. You will not receive any press events
// from the flic before this callback has been sent.
// Typically this event will be sent immediately
// after the flicButtonDidConnect: event.
- (void)flicButtonIsReady:(SCLFlicButton *_Nonnull)button {
    [self sendEventMessage: @{@"event": @"flicButtonIsReady"}];
}

// This delegate method is called every time the flic
// has disconnected, regardless of the reason for it.
// This can sometimes be called during a connection
// event that failed before the user was notified
// of the connection.
- (void)flicButton:(SCLFlicButton *_Nonnull)button didDisconnectWithError:(NSError *_Nullable)error {
    [self sendEventMessage: @{
        @"event": @"didDisconnectWithError",
        @"error": error
    }];
}

// The requested connection failed. Please note that
// depending on at what point in the connection process
// the connection failed you might also receive a
// regular flicButtonDidDisconnect: as well.
// If the connection fails and this callback is made
// then the flic will always cancel the pending connection,
// regardless of what state the flic happens to be in.
// This means that if you get a 
// flicButton:didFailToConnectWithError: 
// then you need to call the connect: 
// yourself to activate the pending connection once again.
- (void)flicButton:(SCLFlicButton *_Nonnull)button didFailToConnectWithError:(NSError *_Nullable)error {
    [self sendEventMessage: @{
        @"event": @"didFailToConnectWithError",
        @"error": error
    }];
}

// This callback verifies (unless an error occurred)
// that the RSSI value was updated.
- (void)flicButton:(SCLFlicButton *_Nonnull)button didUpdateRSSI:(NSNumber *_Nonnull)RSSI error:(NSError *_Nullable)error {
    [self sendEventMessage: @{
        @"event": @"didUpdateRSSI",
        @"RSSI": RSSI,
        @"error": error
    }];
}

@end
  