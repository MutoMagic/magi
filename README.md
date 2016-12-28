# magi

Magi is an unofficial OSU open-source client, based on libGDX framework. Support for Windows, Linux and Android platforms.

Note: At present, there is no test on Linux, may be some problems.

## Building

1.Add the JAVA6_HOME and JAVA8_HOME environment variables.
See [gradle-retrolambda](https://github.com/evant/gradle-retrolambda)

2.Add the [Lombok](https://projectlombok.org/) plugin in your IDE.

3.Running the project.

- [Package application](https://github.com/libgdx/libgdx/wiki/Gradle-on-the-Commandline#packaging-the-project)

- Running the desktop project in IDEA

    Open the Run/Debug Configurations, add Application and set
    
    >Main class = com.moebuff.magi.desktop.DesktopLauncher  
    Working Directory = magi\android\assets

    Save and run.

## Licence

    Copyright 2016 muto

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
