# geotools

Utilities for working with GPS coordinates.


There are 3 representations of a coordinate:

| Type | Description | Example |
| ------- | ----------- | ------- |
| DMS | Degrees, minutes, seconds | `[56 57 6.4]`|
| DDM | Degrees and decimal minutes | `[56 57.1074]` |
| DEC | Decimal degrees |`56.951790` |

* Negative latitudes are _south_
* Netative longitudes are _west_

## Parsing

* Generic coordinate string parsing: `geotools.parse.core/parse-string`
* Latvian Geospatial Information Agency (LĢIA) export parsing
  `geotools.parse.lgia/parse-lgia`
