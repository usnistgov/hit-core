<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<artifactId>hit-core-service</artifactId>
	<packaging>jar</packaging>
	<name>hit-core-service</name>
	<url>http://maven.apache.org</url>
	<parent>
		<groupId>gov.nist.hit.core</groupId>
		<artifactId>hit-core</artifactId>
		<version>1.0.25</version>
	</parent>



	<dependencies>
		<dependency>
			<groupId>gov.nist.hit.core</groupId>
			<artifactId>hit-core-repo</artifactId>
			<version>${project.version}</version>
		</dependency>

		<dependency>
			<groupId>org.xhtmlrenderer</groupId>
			<artifactId>core-renderer</artifactId>
			<version>R8</version>
		</dependency>

		<dependency>
			<groupId>xom</groupId>
			<artifactId>xom</artifactId>
			<version>1.2.5</version>
			<exclusions>
				<exclusion>
					<groupId>com.ibm.icu</groupId>
					<artifactId>icu4j</artifactId>
				</exclusion>
				<exclusion>
					<groupId>xalan</groupId>
					<artifactId>xalan</artifactId>
				</exclusion>
			</exclusions>

		</dependency>

		<dependency>
			<groupId>com.itextpdf</groupId>
			<artifactId>itextpdf</artifactId>
			<version>5.5.6</version>
		</dependency>

		<dependency>
			<groupId>com.itextpdf.tool</groupId>
			<artifactId>xmlworker</artifactId>
			<version>5.5.6</version>
		</dependency>

		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit-dep</artifactId>
			<version>4.11</version>
			<scope>test</scope>
		</dependency>


		<dependency>
			<groupId>com.ibm.icu</groupId>
			<artifactId>icu4j</artifactId>
			<version>3.4.4</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context-support</artifactId>
			<version>${spring.version}</version>
		</dependency>


		<!-- <dependency> <groupId>net.sf.saxon</groupId> <artifactId>saxon-HE</artifactId> 
			<version>9.7.0-4</version> </dependency> -->

		<dependency>
			<groupId>net.sf.saxon</groupId>
			<artifactId>saxon</artifactId>
			<version>8.7</version>
		</dependency>


		<dependency>
			<groupId>net.sf.saxon</groupId>
			<artifactId>saxon-dom</artifactId>
			<version>8.7</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-web</artifactId>
			<version>${spring-security.version}</version>
		</dependency>


		<dependency>
			<groupId>gov.nist.healthcare.hl7.v2.unifiedreport</groupId>
			<artifactId>report</artifactId>
			<version>1.0.5</version>
		</dependency>
	</dependencies>



</project>
