<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<artifactId>hit-core-api</artifactId>
	<packaging>jar</packaging>
	<name>hit-core-api</name>
	<url>http://maven.apache.org</url>
	<parent>
		<groupId>gov.nist.hit.core</groupId>
		<artifactId>hit-core</artifactId>
		<version>1.0.25</version>
	</parent>

	<properties>
		<spring-security.version>3.1.4.RELEASE</spring-security.version>		
	</properties>

	<build>
		<pluginManagement>
			<plugins>

				<!-- plugin to handle compile version -->
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-compiler-plugin</artifactId>
					<version>2.5.1</version>
					<configuration>
						<source>${java.version}</source>
						<target>${java.version}</target>
					</configuration>
				</plugin>

				<plugin>
					<artifactId>maven-eclipse-plugin</artifactId>
					<version>2.9</version>
					<configuration>
						<additionalProjectnatures>
							<projectnature>org.springframework.ide.eclipse.core.springnature</projectnature>
						</additionalProjectnatures>
						<additionalBuildcommands>
							<buildcommand>org.springframework.ide.eclipse.core.springbuilder</buildcommand>
						</additionalBuildcommands>
						<downloadSources>true</downloadSources>
						<downloadJavadocs>true</downloadJavadocs>
					</configuration>
				</plugin>

			</plugins>
		</pluginManagement>
	</build>

	<dependencies>
		<dependency>
			<groupId>gov.nist.hit.core</groupId>
			<artifactId>hit-core-service</artifactId>
			<version>${project.version}</version>
		</dependency>
		<dependency>
			<groupId>gov.nist.hit.core</groupId>
			<artifactId>hit-core-auth-service</artifactId>
			<version>${project.version}</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-webmvc</artifactId>
			<version>${spring.version}</version>
			<exclusions>
				<exclusion>
					<groupId>commons-logging</groupId>
					<artifactId>commons-logging</artifactId>
				</exclusion>
			</exclusions>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-web</artifactId>
			<version>${spring.version}</version>
			<exclusions>
				<exclusion>
					<groupId>commons-logging</groupId>
					<artifactId>commons-logging</artifactId>
				</exclusion>
			</exclusions>
		</dependency>


		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-test</artifactId>
			<version>${spring.version}</version>
			<exclusions>
				<exclusion>
					<groupId>commons-logging</groupId>
					<artifactId>commons-logging</artifactId>
				</exclusion>
			</exclusions>
		</dependency>

		<dependency>
			<groupId>javax.inject</groupId>
			<artifactId>javax.inject</artifactId>
			<version>1</version>
		</dependency>

		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>3.1.0</version>
			<scope>provided</scope>
		</dependency>

		<dependency>
			<groupId>commons-dbcp</groupId>
			<artifactId>commons-dbcp</artifactId>
			<version>1.4</version>
		</dependency>

		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
			<version>3.4</version>
		</dependency>

		<!-- File upload -->
		<dependency>
			<groupId>commons-fileupload</groupId>
			<artifactId>commons-fileupload</artifactId>
			<version>1.2</version>
		</dependency>
		<dependency>
			<groupId>commons-io</groupId>
			<artifactId>commons-io</artifactId>
			<version>2.4</version>
		</dependency>

		<!-- Test libraries -->

		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit-dep</artifactId>
			<version>4.11</version>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>com.jayway.jsonpath</groupId>
			<artifactId>json-path-assert</artifactId>
			<version>0.8.1</version>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.mockito</groupId>
			<artifactId>mockito-core</artifactId>
			<version>1.9.5</version>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.hamcrest</groupId>
			<artifactId>hamcrest-library</artifactId>
			<version>1.3</version>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>gov.nist.healthcare.mu.core.stat</groupId>
			<artifactId>stat-logging</artifactId>
			<version>0.0.1</version>
		</dependency>


		<dependency>
			<groupId>com.ibm.icu</groupId>
			<artifactId>icu4j</artifactId>
			<version>3.4.4</version>
		</dependency>
		<!-- <dependency> <groupId>com.mangofactory</groupId> <artifactId>swagger-springmvc</artifactId> 
			<version>1.0.2</version> </dependency> -->
		<!-- <dependency> <groupId>org.ajar</groupId> <artifactId>swagger-spring-mvc-ui</artifactId> 
			<version>0.3</version> </dependency> -->


		
		<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-test</artifactId>
	<version>${spring.version}</version>
</dependency> 

<dependency>
			<groupId>junit</groupId>
			<artifactId>junit-dep</artifactId>
			<version>4.11</version>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>com.jayway.jsonpath</groupId>
			<artifactId>json-path-assert</artifactId>
			<version>0.8.1</version>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.mockito</groupId>
			<artifactId>mockito-core</artifactId>
			<version>1.9.5</version>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.hamcrest</groupId>
			<artifactId>hamcrest-library</artifactId>
			<version>1.3</version>
			<scope>test</scope>
		</dependency> 
		
		<dependency>
			<groupId>javax.mail</groupId>
			<artifactId>mail</artifactId>
			<version>1.4</version>
		</dependency>
		<dependency>
			<groupId>com.icegreen</groupId>
			<artifactId>greenmail</artifactId>
			<version>1.3.1b</version>
			<scope>test</scope>
		</dependency>
		
<!-- 		<dependency>
    <groupId>com.sun.mail</groupId>
    <artifactId>smtp</artifactId>
    <version>1.4.5</version>
</dependency>
		 -->
		
		
<!-- 		<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-bean-validators</artifactId>
    <version>2.4.0</version>
</dependency> 
-->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.4.0</version>
</dependency>

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-staticdocs</artifactId>
    <version>2.4.0</version>
 </dependency> 
 
	</dependencies>
	<repositories>
		<repository>
      <id>jcenter-snapshots</id>
      <name>jcenter</name>
      <url>https://jcenter.bintray.com/</url>
    </repository>
	</repositories>

</project>