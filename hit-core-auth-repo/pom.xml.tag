<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<artifactId>hit-core-auth-repo</artifactId>
	<packaging>jar</packaging>
	<name>hit-core-auth-repo</name>
	<url>http://maven.apache.org</url>
	<parent>
		<groupId>gov.nist.hit.core</groupId>
		<artifactId>hit-core</artifactId>
		<version>1.0.25</version>
	</parent>

	<dependencies>

		<dependency>
			<groupId>gov.nist.hit.core</groupId>
			<artifactId>hit-core-domain</artifactId>
			<version>${project.version}</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-tx</artifactId>
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
		</dependency>


		<dependency>
			<groupId>org.springframework.data</groupId>
			<artifactId>spring-data-jpa</artifactId>
			<version>1.11.4.RELEASE</version>
			<exclusions>
				<exclusion>
					<groupId>org.springframework</groupId>
					<artifactId>spring-aop</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>


</project>