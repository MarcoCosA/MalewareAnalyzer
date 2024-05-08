## FileIdentify - File Analysis Tool

**FileIdentify** is a Java application designed to perform various file analysis operations. It can check file signatures, upload files to VirusTotal for security analysis, and extract text and metadata using Apache Tika. The application also supports generating detailed reports for each processed file.

### Features
- **File Signature Checking**: Analyze a file's signature and suggest the likely file extension.
- **VirusTotal Integration**: Upload files to VirusTotal and retrieve comprehensive security reports.
- **Text and Metadata Extraction**: Utilize Apache Tika to extract text and metadata from files.
- **Report Generation**: Optionally generate detailed reports and save them to a specified directory.

### Building the Project
To build the project, execute the following script in your terminal:
```bash
./build.sh
```
This script will compile the Java files and package them into a runnable JAR file.

### Usage
Run the application with the file path and desired operations:
```bash
java -jar FileIdentify-0.0.1-SNAPSHOT.jar <file_path> <operation> [--report] [<operation>...]
```

### Example Command
To analyze a directory of files, generate reports, and extract metadata, you would run:
```bash
java -jar FileIdentify-0.0.1-SNAPSHOT.jar Files_to_Analyze -r -a
```

### Available Operations
- `--check-signature, -c`: Check the file signature.
- `--upload-virustotal, -u`: Upload the file to VirusTotal.
- `--analyze-tika, -a`: Analyze the file using Apache Tika.
- `--help, -h`: Display help information.

### Flags
- `--report, -r`: Write output to a report file named based on the input file.
