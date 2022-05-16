<h1 align="center"> ST M24LR Nfc Library</h1>
</br>
<p align="center"> 
  <img src="https://estore.st.com/media/catalog/product/t/s/tssop-8_5.jpg?quality=80&bg-color=255,255,255&fit=bounds&height=700&width=700&canvas=700:700" alt="HAR Logo" width="300px" height="300px">
</p>

 ![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat) ![JITPACK](https://jitpack.io/v/latinosamuel/stm24lrnfclibrary.svg) ![Language](https://img.shields.io/github/languages/top/cortinico/kotlin-android-template?color=blue&logo=kotlin)

<!-- TABLE OF CONTENTS -->
<h2 id="table_of_contents"> :book: Table of Contents</h2>

<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about_the_library"> ➤ About The Library</a></li>
    <li><a href="#features"> ➤ Features</a></li>
    <li>
      <a href="#integration"> ➤ Integration</a>
      <ul>
        <li><a href="#maven">Maven</a></li>
        <li><a href="#gradle">Gradle</a></li>
      </ul>
    </li>
    <li>
      <a href="#sample"> ➤ Sample</a>
      <ul>
        <li><a href="#read_single_block">Read Single Block</a></li>
        <li><a href="#write_single_block">Write Single Block</a></li>
        <li><a href="#read_multiple_blocks">Read Multiple Blocks</a></li>
        <li><a href="#write_multiple_blocks">Write Multiple Blocks</a></li>
      </ul>
    </li>
    <li><a href="#release_note"> ➤ Release Note</a></li>
    <li><a href="#license"> ➤ License</a></li>
  </ol>
</details>

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)

<!-- ABOUT THE LIBRARY -->
<h2 id="about_the_library"> :pencil: About The Library</h2>

<p align="justify"> 
This library has as objective to help you write and read Non-NDEF NFC Tags, more specifically NFCv tags.</br>
While, according to the official Android reference, it is the standard NDEF Tags you can read NdefMessage objects from extra intent and write to an NDEF tag by calling the writeNdefMessage method on the Tag object, it is not that simple when it comes to Non-NDEF NFC Tags: you will need to cope with raw commands and their payloads.</br>
In the tests performed with the library, the M24LR16E-R chip from the STMicroelectronics brand was used.</br>
The M24LR16E-R is organized as 2048 × 8 bits in the I2C mode and as 512 × 32 bits in the ISO 15693 and ISO 18000-3 mode 1 RF mode, 16 sectors of 16 sectors of 32 blocks of 32 bits.
</p>

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)

<!-- FEATURES -->
<h2 id="features"> :dart: Features</h2>
This library has the following features:
</br></br>
➤ Process.READ_SINGLE_BLOCK - Allows you to read a single block of data from the nfcv tag</br>
➤ Process.WRITE_SINGLE_BLOCK - Allows you to write a single block of data from the nfcv tag</br>
➤ Process.READ_MULTIPLE_BLOCK - Allows you to read one or more blocks of data from the nfcv tag</br>
➤ Process.WRITE_MULTIPLE_BLOCK - Allows you to write one or more blocks of data from the nfcv tag</br>


![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)

<!-- INTEGRATION -->
<h2 id="integration"> :hammer: Integration</h2>

Below is a brief guide to using dependency management tools like maven or gradle.

<h4 id="maven">Maven</h4>

Add the JitPack repository to your build file:
```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>
```

To use maven add this dependency to your pom.xml:
```xml
<dependency>
    <groupId>com.github.latinosamuel</groupId>
    <artifactId>STM24LRNfcLibrary</artifactId>
    <version>1.0.0</version>
</dependency>
```

<h4 id="gradle">Gradle</h4>

Add it in your root build.gradle at the end of repositories:
```xml
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Then you can just add the latest version to your build.
```xml
implementation 'com.github.latinosamuel:STM24LRNfcLibrary:1.0.0'
```

If you do not use any dependency management tool, you can find the latest standalone aar [here](https://github.com/latinosamuel/STM24LRNfcLibrary/releases/latest).

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)

<!-- SAMPLE -->
<h2 id="sample"> :mag: Sample</h2>

<h4 id="read_single_block">Read Single Block:</h4>

```kotlin
  // Block number to read
  val blockNumber = 0
  M24LRInit().init(this@MainActivity, Process.READ_SINGLE_BLOCK, blockNumber)
      .completedListener(object : CompletedListener {
          override fun onProcessCompletedReadSingleBlock(blockNumber: Int, hexData: String) {
			        ...
          }
      }).errorListener(object : ErrorListener {
          override fun onProcessTerminated(terminationReason: TerminationReason, reason: String) {
              ...
          }

          override fun onProcessInterrupted(interruptReason: InterruptReason) {
              ...
          }
      }).iTagListener(object : ITagListener {
          override fun onTagFound() {
              ...
          }
      }).build(this@MainActivity)
```

<h4 id="write_single_block">Write Single Block:</h4>

```kotlin
  //Block number to write
  val blockNumber = 0
  //Data to write in the respective block in hexadecimal
  val data = "01506201"

  M24LRInit().init(this@MainActivity, Process.WRITE_SINGLE_BLOCK, blockNumber, data)
      .completedListener(object : CompletedListener{
          override fun onProcessCompletedWriteSingleBlock() {
              ...
          }
      }).errorListener(object :  ErrorListener{
          override fun onProcessTerminated(terminationReason: TerminationReason, reason: String) {
              ...
          }
          override fun onProcessInterrupted(interruptReason: InterruptReason) {
              ...
          }
      }).iTagListener(object : ITagListener{
          override fun onTagFound() {
              ...
          }
      }).build(this@MainActivity)
```

<h4 id="read_multiple_blocks">Read Multiple Blocks:</h4>

```kotlin
  //List with the number of blocks you want to read
  val blockNumberList = arrayListOf(0,1,2,3,4)
            
  M24LRInit().init(this@MainActivity, Process.READ_MULTIPLE_BLOCK, blockNumberList)
      .completedListener(object : CompletedListener{
          override fun onProcessCompletedReadMultipleBlock(response: HashMap<Int, String>) {
              ...
          }
      }).errorListener(object : ErrorListener{
          override fun onProcessTerminated(terminationReason: TerminationReason, reason: String) {
              ...
          }
          override fun onProcessInterrupted(interruptReason: InterruptReason) {
              ...
          }
      }).iTagListener(object : ITagListener{
          override fun onTagFound() {
              ...
          }
      }).build(this@MainActivity)
```

<h4 id="write_multiple_blocks">Write Multiple Blocks:</h4>

```kotlin
  //Hashmap example with block number and date in hex
  val map = HashMap<Int,String>()
  map[0]= "01506201"
  map[1]= "FFFFFFFF"
  map[2]= "FFF0FFFF"
  map[3]= "FFFFFFFF"
  map[4]= "FFF00003"

  M24LRInit().init(this@MainActivity, Process.WRITE_MULTIPLE_BLOCK, map)
      .completedListener(object : CompletedListener{
          override fun onProcessCompletedWriteMultipleBlock() {
              ...
          }
      }).errorListener(object :  ErrorListener{
          override fun onProcessTerminated(terminationReason: TerminationReason, reason: String) {
              ...
          }
          override fun onProcessInterrupted(interruptReason: InterruptReason) {
              ...
          }
      }).iTagListener(object : ITagListener{
          override fun onTagFound() {
              ...
          }
      }).build(this@MainActivity)
```

To see the full sample [click here](https://github.com/latinosamuel/STM24LRNfcLibrary/tree/master/sample).

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)

<!-- RELEASE NOTE -->
<h2 id="release_note"> :notebook: Release Note</h2>

See release notes on [github releases](https://github.com/latinosamuel/STM24LRNfcLibrary/releases).

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)

<!-- LICENSE -->
<h2 id="license"> :link: License</h2>

    Copyright 2021 Samuel Latino
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

