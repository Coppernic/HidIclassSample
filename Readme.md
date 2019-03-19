- **dev** : [![pipeline status](https://gitlab.coppernic.fr/github/HidIclassSample/badges/dev/pipeline.svg)](https://gitlab.coppernic.fr/github/HidIclassSample/commits/dev)
- **master** : [![pipeline status](https://gitlab.coppernic.fr/github/HidIclassSample/badges/master/pipeline.svg)](https://gitlab.coppernic.fr/github/HidIclassSample/commits/master)

Hid Iclass Sample Project
========================

# Hid Iclass Sample

This sample shows how to use the Hid Iclass reader on C-One².

## Prerequisites
Core Services sall be installed on your device. PLease install the last version on F-Droid if it is not installed.

## Set Up

Install in your build.gradle:

```
repositories {
    jcenter()
    maven { url 'https://artifactory.coppernic.fr/artifactory/libs-release' }
}

dependencies {
// [...]
    implementation 'fr.coppernic.sdk.cpcutils:CpcUtilsLib:6.18.1'
    implementation 'fr.coppernic.sdk.core:CpcCore:1.8.6'
    implementation 'fr.coppernic.sdk.hid.iclassProx:CpcHidIClassProx:2.0.0'
// [...]
}
```

### Power management

 * Use Power Single

```java

   ConePeripheral.RFID_HID_ICLASSPROX_GPIO.descriptor.power(context, on)

```


### Reader initialization

#### Create reader object
 * Declare a Reader object

```java
lateinit var reader: Reader
```
 * Create a listener 
 
```java
private val instanceListener= object: InstanceListener<Reader>{
        override fun onDisposed(p0: Reader) {
        }

        override fun onCreated(hidReader: Reader) {
            Timber.d("hid reader instance created")
            reader = hidReader
            if(!completableEmitter.isDisposed) {
                completableEmitter.onComplete()
            }
        }
    }
```
 * Instantiate it after power up:

```java
Reader.getInstance(context, instanceListener)
```


### Open reader
```java
 var res = reader.open(CpcDefinitions.HID_ICLASS_PROX_READER_PORT, BaudRate.B9600)
```

## Read tag
```java
  val res = reader.samCommandScanFieldForCard(frameProtocolList, card)
  if(res != ErrorCodes.ER_OK){
  	Timber.d("error read")
  }else{
 	//get your data in card object
  }
```

## License

    Copyright (C) 2018 Coppernic

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

