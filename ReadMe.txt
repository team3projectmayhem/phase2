How to import this application
1. Unzip team11phase2.zip
2. Go to Android Studio
3. Go to File -> import project
4. Select "App" from inside the UNZIPPED team11phase1 file
5. After the file is imported, there will be a lot of red in the code.
6. Go try to build the project and it will fail.
7. The build.gradle will pop up with a lot of random code.
8. type in at the bottom of the build.gradle file

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.0.+'
    }
}

9. rebuild project
10. The project should work perfectly
11. Run the app on what ever emulator you like.