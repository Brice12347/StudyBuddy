//package com.example.studybuddy;
//
//import android.os.Looper;
//
//public static androidx.test.espresso.IdlingResource waitFor(final long millis) {
//    return new androidx.test.espresso.IdlingResource() {
//        private ResourceCallback resourceCallback;
//        private boolean isIdle = false;
//
//        @Override
//        public String getName() {
//            return WaitIdlingResource.class.getName() + millis;
//        }
//
//        @Override
//        public boolean isIdleNow() {
//            if (isIdle) {
//                return true;
//            }
//            new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                isIdle = true;
//                if (resourceCallback != null) {
//                    resourceCallback.onTransitionToIdle();
//                }
//            }, millis);
//            return false;
//        }
//
//        @Override
//        public void registerIdleTransitionCallback(ResourceCallback callback) {
//            this.resourceCallback = callback;
//        }
//    };
//}
