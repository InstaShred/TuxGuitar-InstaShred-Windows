/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class javelin_javelin */

#ifndef _Included_javelin_javelin
#define _Included_javelin_javelin
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     javelin_javelin
 * Method:    listBLEDevices
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_javelin_javelin_listBLEDevices
  (JNIEnv *, jclass);

/*
 * Class:     javelin_javelin
 * Method:    getBLEDeviceName
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javelin_javelin_getBLEDeviceName
  (JNIEnv *, jclass, jstring);

/*
 * Class:     javelin_javelin
 * Method:    listBLEDeviceServices
 * Signature: (Ljava/lang/String;)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_javelin_javelin_listBLEDeviceServices
  (JNIEnv *, jclass, jstring);

/*
 * Class:     javelin_javelin
 * Method:    listBLEServiceCharacteristics
 * Signature: (Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_javelin_javelin_listBLEServiceCharacteristics
  (JNIEnv *, jclass, jstring, jstring);

/*
 * Class:     javelin_javelin
 * Method:    getBLECharacteristicValue
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)[B
 */
JNIEXPORT jbyteArray JNICALL Java_javelin_javelin_getBLECharacteristicValue
  (JNIEnv *, jclass, jstring, jstring, jstring);

/*
 * Class:     javelin_javelin
 * Method:    setBLECharacteristicValue
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[B)Z
 */
JNIEXPORT jboolean JNICALL Java_javelin_javelin_setBLECharacteristicValue
  (JNIEnv *, jclass, jstring, jstring, jstring, jbyteArray);

/*
 * Class:     javelin_javelin
 * Method:    watchBLECharacteristicChanges
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_javelin_javelin_watchBLECharacteristicChanges
  (JNIEnv *, jclass, jstring, jstring, jstring);

/*
 * Class:     javelin_javelin
 * Method:    clearBLECharacteristicChanges
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_javelin_javelin_clearBLECharacteristicChanges
  (JNIEnv *, jclass, jstring, jstring, jstring);

/*
 * Class:     javelin_javelin
 * Method:    waitForBLECharacteristicChanges
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)[B
 */
JNIEXPORT jbyteArray JNICALL Java_javelin_javelin_waitForBLECharacteristicChanges
  (JNIEnv *, jclass, jstring, jstring, jstring, jint);

/*
 * Class:     javelin_javelin
 * Method:    unWatchBLECharacteristicChanges
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_javelin_javelin_unWatchBLECharacteristicChanges
  (JNIEnv *, jclass, jstring, jstring, jstring);

#ifdef __cplusplus
}
#endif
#endif