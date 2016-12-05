create table building(buildingid varchar2(50), buildingname varchar2(50),  shape SDO_GEOMETRY, PRIMARY KEY(buildingname));
create table firehydrant(firehydrantid varchar2(50), shape SDO_GEOMETRY, Primary key (firehydrantid));
create table firebuilding(name varchar2(50), FOREIGN KEY(name) REFERENCES building(buildingname));
	
	INSERT INTO USER_SDO_GEOM_METADATA(TABLE_NAME, COLUMN_NAME, DIMINFO, SRID) VALUES('building', 'shape',SDO_DIM_ARRAY(SDO_DIM_ELEMENT('X', 0, 820, 0.005), SDO_DIM_ELEMENT('Y', 0, 580, 0.005)), NULL);

	INSERT INTO USER_SDO_GEOM_METADATA(TABLE_NAME, COLUMN_NAME, DIMINFO, SRID) VALUES('firehydrant', 'shape',SDO_DIM_ARRAY(SDO_DIM_ELEMENT('X', 0, 820, 0.005), SDO_DIM_ELEMENT('Y', 0, 580, 0.005)), NULL);
	
	
		
	CREATE INDEX buildingindex
	ON building(shape)
	INDEXTYPE IS MDSYS.SPATIAL_INDEX;	
	
	
	CREATE INDEX firehydrantindex
	ON firehydrant(shape)
	INDEXTYPE IS MDSYS.SPATIAL_INDEX;
	
	