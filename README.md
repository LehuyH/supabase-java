# Supabase-Java 🚀

Supabase-Java is a Java library that simplifies interaction with the Supabase.

## Features ✨

- **User Authentication:** Perform user authentication operations.
- **Serverless Functions:** Invoke serverless functions.
- **PostgREST Interaction:** Interact with the Supabase database using the PostgREST API. Supports all filters and operations.
- **JavaScript Like Syntax:** Closely matches the chaining syntax of `supabase-js`


## Installation 🛠️

To use Supabase-Java in your Java project:

0. **Get your Supabase Credentials:**

   If you are not using a self-hosted / local instance, you can use the [hosted version](https://supabase.com/dashboard)
   
1. **Download Source Code:**

   Clone or download the source code of this library from the repository.

2. **Include in Your Project:**

   - Include the downloaded `supabase-java` source code into your project structure.
   - Adjust your project configuration (e.g., Maven or Gradle) to include the library source files.


## Usage 🚀

Using Supabase-Java is straightforward. Here's some common use cases:

1. **Initialize Supabase Client:**

   ```java
   SupabaseClient supabase = new SupabaseClient("https://your-supabase-url.com", "your-api-key");
   ```

2. **Configure Authentication:**

   ```java
   // Access the auth instance
   GoTrue auth = supabase.auth;

   // Example: Sign up a new user
   auth.signUp("user@example.com", "password123");
   ```

3. **Invoke Serverless Functions:**

   ```java
   // Access the functions client
   FunctionsClient functionsClient = supabase.functions;

   // Example: Invoke a serverless function
   String result = functionsClient.invoke("functionName");
   ```

4. **Work with the Database:**

   ```java
   // Access the Postgrest client
   PostgrestClient postgrestClient = supabase.from("tasks");

   // Example: Select query with filters and ordering
   JSONObject response = postgrestClient
       .select("id, task_name, completed")
       .eq("completed", "false")
       .order("created_at")
       .limit(10)
       .exec();
   ```

## Contributing 🤝

Contributions, bug reports, and feature requests are welcome! Feel free to open issues and pull requests.

## License 📄

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---


### `SupabaseClient`

The `SupabaseClient` class is the main entry point for interacting with Supabase in Java.

#### Constructors

- `SupabaseClient(String supabaseUrl, String supabaseKey)`: Initializes the Supabase client with the Supabase URL and API key.

#### Accessing Subclasses

- `auth`: Instance of `GoTrue` class for handling user authentication.
- `functions`: Instance of `FunctionsClient` for invoking serverless functions.
- `from`: Function that returns an instance of `PostgrestClient` for handling database requests



### `PostgrestClient`

The `PostgrestClient` class provides functionalities to interact with the Supabase database using the PostgREST API.

#### Methods

- Methods for building queries to interact with the database by specifying select queries, filters, ordering, limits, inserts, updates, and deletions.

As the `PostgrestClient` is accessed through an instance of `SupabaseClient`, here's an overview of how to use it:

```java
SupabaseClient supabase = new SupabaseClient("https://your-supabase-url.com", "your-api-key");
PostgrestClient postgrestClient = supabase.from("table_name");
```

The following methods can be used on `PostgrestClient`:

#### Query Building Methods

- `select(String selectQuery)`: Specifies the columns to retrieve.
- `eq(String column, String value)`: Filters by equality.
- `neq(String column, String value)`: Filters by inequality.
- `gt(String column, String value)`: Filters by greater than.
- `gte(String column, String value)`: Filters by greater than or equal to.
- `lt(String column, String value)`: Filters by less than.
- `lte(String column, String value)`: Filters by less than or equal to.
- `like(String column, String value)`: Filters by pattern matching.
- `ilike(String column, String value)`: Filters by case-insensitive pattern matching.
- `is(String column, String value)`: Filters by null or not null values.
- `in(String column, T[] values)`: Filters by inclusion in an array.
- `contains(String column, T[] values)`: Filters by containing elements in an array.
- `containedBy(String column, T[] values)`: Filters by being contained by another array.
- `rangeGt(String column, T[] range)`: Filters by range greater than.
- `rangeGte(String column, T[] range)`: Filters by range greater than or equal to.
- `rangeLt(String column, T[] range)`: Filters by range less than.
- `rangeLte(String column, T[] range)`: Filters by range less than or equal to.
- `rangeAdjacent(String column, T[] range)`: Filters by adjacent range.
- `overlaps(String column, T[] range)`: Filters by overlapping range.
- `textSearch(String column, String query, PostgrestFullTextSearchOptions options)`: Performs a full-text search.
- `match(Map<String, T> columnValueMap)`: Filters by matching multiple columns to specified values.
- `not(String column, String operator, String value)`: Negates a filter.
- `or(String orFilters)`: Combines multiple filters using logical OR.
- `or(String orFilters, String foreignTable)`: Combines multiple filters on a foreign table using logical OR.
- `filter(String column, String operator, String value)`: Applies a custom filter.
- `order(String column, PostgrestOrderOptions options)`: Orders the result set by column.
- `order(String column)`: Orders the result set by column (default ordering).
- `limit(int limit)`: Limits the number of records returned.
- `limit(int limit, String foreignTable)`: Limits the number of records returned from a foreign table.
- `range(int from, int to)`: Limits the records returned within a specified range.
- `range(int from, int to, String foreignTable)`: Limits the records returned from a foreign table within a specified range.
- `single()`: Limits the result to a single record.
- `csv()`: Requests data in CSV format.
- `insert(JSONObject body)`: Inserts a new record.
- `insert(JSONArray body)`: Inserts multiple records.
- `upsert(JSONObject body)`: Upserts a record (inserts or updates).
- `upsert(JSONArray body)`: Upserts multiple records.
- `update(JSONObject body)`: Updates existing records.
- `delete()`: Deletes records.

Example usage of `PostgrestClient` methods:

```java
PostgrestClient postgrestClient = supabase.from("tasks");

// Example: Select query with filters and ordering
JSONObject response = postgrestClient
    .select("id, task_name, completed")
    .eq("completed", "false")
    .limit(10)
    .exec();

// Example: Inserting a new record
JSONObject newTask = new JSONObject();
newTask.put("task_name", "Sample Task");
newTask.put("completed", false);
JSONObject insertResponse = postgrestClient.insert(newTask).exec();
```



### `Authentication With GoTrue`

The `GoTrue` class handles user authentication functionalities within the SupabaseClient.

#### Methods

- `getSession()`: Retrieves the user session.
- `signUp(String email, String password)`: Registers a new user with the provided email and password.
- `signInWithPassword(String email, String password)`: Authenticates a user using email and password.
- `getUser()`: Retrieves user information.

Example:
```java
SupabaseClient supabase = new SupabaseClient("https://your-supabase-url.com", "your-api-key");
// Accessing GoTrue instance from SupabaseClient
GoTrue auth = supabase.auth;

// Usage examples of GoTrue methods
JSONObject session = auth.getSession();
auth.signUp("user@example.com", "password123");
auth.signInWithPassword("user@example.com", "password123");
JSONObject user = auth.getUser();
```

### `FunctionsClient`

The `FunctionsClient` class facilitates invoking serverless functions provided by Supabase.

#### Methods

- `invoke(String name)`: Invokes a serverless function by name.
- `invoke(String name, JSONObject body)`: Invokes a serverless function with a request body.
- `invoke(String name, JSONObject body, JSONObject headers)`: Invokes a serverless function with specified headers.
- `invoke(String name, JSONObject body, JSONObject headers, String method)`: Invokes a serverless function with specified headers and HTTP method.

Example:
```java
SupabaseClient supabase = new SupabaseClient("https://your-supabase-url.com", "your-api-key");
// Accessing FunctionsClient instance from SupabaseClient
FunctionsClient functionsClient = supabase.functions;

// Usage examples of FunctionsClient methods
String result1 = functionsClient.invoke("functionName");
JSONObject requestBody = new JSONObject();
String result2 = functionsClient.invoke("functionName", requestBody);
JSONObject requestHeaders = new JSONObject();
String result3 = functionsClient.invoke("functionName", requestBody, requestHeaders);
String result4 = functionsClient.invoke("functionName", requestBody, requestHeaders, "POST");
```

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.
