# namespacehandler
Repository for parsing namespaces for Glygen metadata such as species, cell lines, diseases etc. to be used as part of typeahead functionality and validation in GlyTableMaker or other applications.

Currently supported dictionaries:
*  NCBI Taxonomy: download [new_taxdump.tar.gz](https://ftp.ncbi.nih.gov/pub/taxonomy/new_taxdump/new_taxdump.tar.gz) and extract names.dmp
*  Cellosaurus Ontology: dowload [cellosaurus.txt](https://ftp.expasy.org/databases/cellosaurus/cellosaurus.txt)
*  UBERON: download [uberon-base.json](http://purl.obolibrary.org/obo/uberon/uberon-base.json)
*  Human Disease Ontology: download [doid-base.json](https://github.com/DiseaseOntology/HumanDiseaseOntology/blob/main/src/ontology/doid-base.json)
*  Human Phenotype Ontology: download [hp.json](https://github.com/obophenotype/human-phenotype-ontology/releases/download/v2024-04-04/hp.json)

After downloading the files, place them in a folder and use the folder name as the argument while running the following java applications to generate the formatted dictionaries. If you would like to run the following without providing an argument, create a folder named "original" in the top level folder of the repository and place the downloaded files there.

There are 3 Java applications to execute. 
* [NCBITaxonomyParser.java](https://github.com/glygener/namespacehandler/blob/main/src/main/java/org/glygen/namespacehandler/NcbiTaxonomyParser.java) - parses the "names.dmp" file from the folder (command line argument) and generates species.txt file in [namespaces](https://github.com/glygener/namespacehandler/tree/main/namespaces) folder of the repository.
* [CellineParser.java](https://github.com/glygener/namespacehandler/blob/main/src/main/java/org/glygen/namespacehandler/CellineParser.java) - parses the "cellosaurus.txt" from the given folder and generates cellline.txt file in [namespaces](https://github.com/glygener/namespacehandler/tree/main/namespaces) folder of the repository.
* [OBOParser.java](https://github.com/glygener/namespacehandler/blob/main/src/main/java/org/glygen/namespacehandler/OBOParser.java) - parses the files with .json extension in the given folder and generates the corresponding .txt versions in [namespaces](https://github.com/glygener/namespacehandler/tree/main/namespaces) folder of the repository.

The generated files are tab separated text files in the following format
```Synonym  Name  URI``` where Synonym is the synonym or equivalent name, Name is the name to be stored and URI is either the ontology URI or the web link to the entry. Last part of the URI contains the ontology identifier or the original identifer used (eg. taxonomy id for NCBI Taxonomy, accession number for Cellosaurus).
