# The Spring Library

A standard library for Java Spring Boot applications, ported from `the_lib` (PHP) and `the_deno`.

## Features

- **SqlBuilder**: Fluent SQL generation with `Left Join` capabilities.
- **Model**: Base class for Entity-like database operations.
- **Auth**: Logic for Authentication and Hashing.
- **FileAct**: Helper class for file uploads and storage management.
- **Mail**: Email sending wrapper.
- **Response**: Standardized API response structures.
- **Session**: Session management wrapper.

## Installation

Add the dependency to your `pom.xml` or `build.gradle`.

## Usage

### 1. Database Models

```java
import com.puneetxp.lib.Model;

public class User extends Model {
    public User(DB db) {
        super(db);
        this.table = "users";
    }
}

// Usages
User user = new User(db);
user.join(Map.of("roles", "roles"), null);
```

### 2. File Uploads

```java
import com.puneetxp.lib.FileAct;

FileAct fileAct = FileAct.init("../storage");
Object result = fileAct.upload(multipartFile, "filename");
```
