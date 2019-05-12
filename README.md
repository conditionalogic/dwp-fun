# test-app - A solution for the DWP/BPDTS assessment
This is a candidate solution the DWP problem @ https://bpdts-test-app.herokuapp.com
It is a multi-module project that includes Swagger code-generation from a fixed version of the Swagger/OpenAPI JSON at the above URL.
Swagger code-generation and a (thin) wrapper around generated code is available using both:
- Spring RestTemplate
- Retrofit2

Generated code is available in test-app-retrofit-client and test-app-spring-client. The corresponding wrappers are in test-app-retrofit-api and test-app-spring-api.
JUnit tests are available for this and they exercise the code both against a fake server, and optionally against a real server.
The test-app-locate-users module uses the Retrofit client code (test-app-retrofit-api) to locate users who are either associated with London, or are associated with a location that is (approximately) within 50 miles from London.
The uber-jar output of this module can be run to get a list of these users.
