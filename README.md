# HACEP
HACEP Core project

# Build instructions
To build the HACEP core code you need Maven. Default builds code using the Red Hat supported repositories and EAP 6.4.5 see [Build with EAP 6.4.5](#build_with_red_hat_supported_eap_645), is there also an EAP 7.0.2 profile to use see [Build with EAP 7.0.2](#build_with_red_hat_eap_702).  

## Build with community dependencies
Alternatively, you can build the code using community repository; simply add the "community" profile to the Maven command:

```shell
mvn clean install -P community
```

## Build with Red Hat supported EAP 6.5.4
First of all you need to download and install the supported dependencies:

### Install the Red Hat supported Maven repositories
If you want to use the Red Hat supported bits, you must install JDG/BRMS/EAP repos. To do so download the following archives (from Red Hat customer portal):
* [JDG 7.0 maven repository](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=45411&product=data.grid)
* [BRMS 6.4.0 maven repository](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=48311&product=brms)
* [EAP 6.4 maven repository](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=37363&product=appplatform)
* [EAP 6.4.5 incremental maven repository](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=40881&product=appplatform)

and extract them all in one place (let's call it as an example `/path/to/extracted/repos`) each one under a different folder named as the archive; at the end of the process you should have a folder structure similar to this:
```shell
/path/to/extracted/repos
|__ /jboss-brms-bpmsuite-6.4.0.GA-maven-repository
|__ /jboss-datagrid-7.0.0-maven-repository
|__ /jboss-eap-6.4.0.GA-maven-repository
|__ /jboss-eap-6.4.5-incremental-maven-repository
```

For your reference, you will find an example `settings.xml` to copy in your .m2 directory from the `example-maven-settings` directory.
Or you can just use it from it is passing this maven option: `-s example-maven-settings/settings.xml`

This Maven settings.xml assumes you have unzipped all your repositories in one folder referenced in the env variable `HACEP_REPO`, you must set it before running `mvn` commands with:
```shell
Linux/OSX: export HACEP_REPO=/path/to/extracted/repos
Windows: set HACEP_REPO=c:\path\to\extracted\repos
```

Then you can build with:
```shell
mvn -s example-maven-settings/settings.xml clean install
```

### Build with Docker
If you like a more isolated environment and a one liner you can build using docker with (remember to change `</path/to/extracted/repos>`):
```shell
docker run -it --rm -u $(id -u):$(id -g) --name hacep \
    -v "$PWD":/tmp/hacep \
    -v </path/to/extracted/repos>:/tmp/haceprepo \
    -e HACEP_REPO=/tmp/haceprepo -w /tmp/hacep maven:3.3.3-jdk-8 \
    mvn -Duser.home=/tmp/maven -s ./example-maven-settings/settings.xml clean install
```

to speed things up, but having less isolation, you can also pass as a volume your `.m2/repository` directory (remember to change both `</path/to/extracted/repos>` and `</path/to/your/.m2/repository>`):
```shell
docker run -it --rm -u $(id -u):$(id -g) --name hacep \
    -v "$PWD":/tmp/hacep \
    -v </path/to/extracted/repos>:/tmp/haceprepo \
    -v </path/to/your/.m2/repository>:/tmp/maven/.m2/repository \
    -e HACEP_REPO=/tmp/haceprepo -w /tmp/hacep maven:3.3.3-jdk-8 \
    mvn -Duser.home=/tmp/maven -s ./example-maven-settings/settings.xml clean install
```

## Build with Red Hat EAP 7.0.2
First of all you need to download and install the supported dependencies:

### Install the Red Hat supported Maven repositories
If you want to use the Red Hat bits, you must install JDG/BRMS/EAP repos. To do so download the following archives (from Red Hat customer portal):
* [JDG 7.0 maven repository](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=45411&product=data.grid)
* [BRMS 6.4.0 maven repository](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=48311&product=brms)
* [EAP 7.0 maven repository](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=43861&product=appplatform)
* [EAP 7.0.2 incremental maven repository](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=46391&product=appplatform)

and extract them all in one place (let's call it as an example `/path/to/extracted/repos`) each one under a different folder named as the archive; at the end of the process you should have a folder structure similar to this:
```shell
/path/to/extracted/repos
|__ /jboss-brms-bpmsuite-6.4.0.GA-maven-repository
|__ /jboss-datagrid-7.0.0-maven-repository
|__ /jboss-eap-7.0.0.GA-maven-repository
|__ /maven-repository
```

For your reference, you will find an example `settingsEAP7.xml` to copy in your .m2 directory from the `example-maven-settings` directory.
Or you can just use it from it is passing this maven option: `-s example-maven-settings/settingsEAP7.xml`

This Maven settings.xml assumes you have unzipped all your repositories in one folder referenced in the env variable `HACEP_REPO`, you must set it before running `mvn` commands with:
```shell
Linux/OSX: export HACEP_REPO=/path/to/extracted/repos
Windows: set HACEP_REPO=c:\path\to\extracted\repos
```

Then you can build with:
```shell
mvn -s example-maven-settings/settingsEAP7.xml clean install
```

### Build with Docker
If you like a more isolated environment and a one liner you can build using docker with (remember to change `</path/to/extracted/repos>`):
```shell
docker run -it --rm -u $(id -u):$(id -g) --name hacep \
    -v "$PWD":/tmp/hacep \
    -v </path/to/extracted/repos>:/tmp/haceprepo \
    -e HACEP_REPO=/tmp/haceprepo -w /tmp/hacep maven:3.3.3-jdk-8 \
    mvn -Duser.home=/tmp/maven -s ./example-maven-settings/settingsEAP7.xml clean install
```

to speed things up, but having less isolation, you can also pass as a volume your `.m2/repository` directory (remember to change both `</path/to/extracted/repos>` and `</path/to/your/.m2/repository>`):
```shell
docker run -it --rm -u $(id -u):$(id -g) --name hacep \
    -v "$PWD":/tmp/hacep \
    -v </path/to/extracted/repos>:/tmp/haceprepo \
    -v </path/to/your/.m2/repository>:/tmp/maven/.m2/repository \
    -e HACEP_REPO=/tmp/haceprepo -w /tmp/hacep maven:3.3.3-jdk-8 \
    mvn -Duser.home=/tmp/maven -s ./example-maven-settings/settingsEAP7.xml clean install
```

# Run an HACEP example
Please refer to [hacep-examples Readme.md](hacep-examples/README.md) for detailed instructions on how to run an HACEP example.
