# Regex
## Checking variable names
```
^[+#-] [a-z].[a-zA-Z]* \: [A-Za-z]*( [=] [a-zA-Z0-9]{0,9})?$
```

## Checking Methods
```
^[+#-] [a-z].[a-zA-Z]*\(([a-z].[a-zA-Z]*(\[\])? \: [A-Za-z]*(, )?)*\)( \: [a-zA-Z]*)?
```

## TODO: Split the validation into actual data to be passed to classRectangle attributes