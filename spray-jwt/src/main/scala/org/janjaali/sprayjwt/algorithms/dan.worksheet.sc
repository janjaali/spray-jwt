val encoder = java.util.Base64.getEncoder()

val urlEncoder = java.util.Base64.getEncoder()

val json = "{\"typ\":\"JWT\",\r\n \"alg\":\"HS256\"}"

val jsonBytes = json.getBytes("UTF-8")

val encodedBytes = urlEncoder.encode(jsonBytes)

val result = new String(encodedBytes)

val expected = "eyJ0eXAiOiJKV1QiLA0KICJhbGciOiJIUzI1NiJ9"

result == expected
