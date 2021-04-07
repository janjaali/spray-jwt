val encoder = java.util.Base64.getEncoder()

val urlEncoder = java.util.Base64.getUrlEncoder()

val json = """{"typ":"JWT","alg":"HS256"}"""

val jsonBytes = json.getBytes("UTF-8")

val encodedBytes = urlEncoder.encode(jsonBytes)

new String(encodedBytes)
