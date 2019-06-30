$env:path = $env:path + ";.\graphVIZ"
rm source.txt
$javaSourceFiles = get-childitem -Recurse -Filter *.java
foreach($javafile in $javaSourceFiles){
    $javafile.FullName >> source.txt
}
javac `@source.txt -encoding utf-8 -d out
java -classpath out com.compilerExp.CLI