

# 配置TVAPI库依赖及相关环境

## 1.引用TV-API库

### 1.1 使用CVTE内部MVN仓库方式
修改/tvdemoapp/build.gradle

    //CVTE CODE START
    //添加用于连接与发布到CVTE MAVEN仓库http://maven.gz.cvte.cn:8081/nexus
    apply plugin: 'maven'

    def getReleaseRepositoryUrl() {
        return hasProperty('RELEASE_REPOSITORY_URL') ? RELEASE_REPOSITORY_URL
                : "http://maven.gz.cvte.cn:8081/nexus/content/repositories/releases/"
    }

    def getSnapshotRepositoryUrl() {
        return hasProperty('SNAPSHOT_REPOSITORY_URL') ? SNAPSHOT_REPOSITORY_URL
                : "http://maven.gz.cvte.cn:8081/nexus/content/repositories/snapshots/"
    }
    //CVTE CODE END

    allprojects {
        repositories {
            //CVTE CODE START
            //添加用于连接与发布到CVTE MAVEN仓库http://maven.gz.cvte.cn:8081/nexus
            mavenLocal()
            maven { url getReleaseRepositoryUrl() }
            maven { url getSnapshotRepositoryUrl() }
            mavenCentral()
            //CVTE CODE END
            jcenter()
        }
    }
修改/tvdemoapp/app/build.gradle配置脚本代码,在dependencies{}里添加TV-API库引用.TVAPI版本更新请自行修改最后的TV-API版本号.

        dependencies {
            ...
            compile 'cn.cvte.tv.api:cvte-tv-api:0.2@aar'
            ...
        }
### 1.2 直接使用JAR包方式.
直接使用JAR包请将tv-api的JAR包放到/lib/下.并确认在/tvdemoapp/app/build.gradle的在dependencies{}里边存在以下依赖

               dependencies {
                   ...
                    compile fileTree(dir: 'libs', include: ['*.jar'])
                   ...
               }
JAR包到 http://maven.gz.cvte.cn:8081/nexus/index.html#nexus-search;quick~cvte-tv-api 下载最新的版本.点击左右角Artifact的最下边Contained In Repositories栏里边的Releases,在左边的Browse Storage栏里查到对应的jar包文件.然后右键DOWNLOAD下载

## 2.修改/tvdemoapp/app/src/main/AndroidManifest.xml

在application标签中添加启动application调用

        <application
            android:name="com.cvte.tv.api.TvApiApplication"
            ...
            >
            ...
        </application>
