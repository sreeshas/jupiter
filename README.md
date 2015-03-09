README for jupiter
==========================



Jupiter is a cab management solution which has the following features

    1) Search for the nearest cabs.
    2) Add/Update the location of a cab.
    3) Find a cab based on its Id.
    4) Delete a cab.

    Search:

    Current location of the user is determined by the browser and is automatically filled in.
    User can increase/decrease the search radius and it is reflected in the UI immediately.
    Search performs geo-spatial query using Mongodb which calculates distance based on spherical geometry.

    Add/Update:

    Click anywhere on the map and an 'unsaved cab' icon is placed on the location clicked. User can manually enter the
    ID of the clicked cab and add it into the system. 'unsaved cab' will be saved and cab icon changes to reflect the same.

    Find:
    User can find the location of any cab based on unique ID of the cab. if the cab is in the visible area of the map,
    searched cab is centered on the map with slight animation effect. In other positive cases, map is updated with cab
    in the center.

    Delete:
    User can delete the cab from the system based on the ID. Updated map will be shown after successful delete.


    Performance and Admin only features.

    User can get an overview of performance of the system after authenticating with admin credentials.
    Username: admin
    Password: admin

    Administrator can
        1) View interesting JVM metrics
             Heap Memory usage of the system and other JVM related data.
        2) Get statistics on events
             Total number of events occurring in the system. It is computed using exponentially weighted moving average
             over past 1 minute, 5 minute and 15 minute windows. It gives an overview of number of recent events
             occurring in the system in real time.
        3) Get Service Statistics
             Gives list of services advertised by the system, their total usage and their response time with
             percentile values.


    <==Technology Stack:==>

    Frontend: Angular JS (1.3.x)
    Web Tier: Java and Spring MVC (1.8 and 4.x)
    Backend: MongoDB (2.6.x)

    The above stack is powered by JHipster/Drop Wizard which is really nice tool for quick prototypes.
    Repository design pattern is used for Web Tier for CRUD.

    The following are relevant(domain related) files in the source code.
    jupiter / src / main / java / com / jupiter / application / web / rest / CabsResource.java
    jupiter / src / main / java / com / jupiter / application / web / rest / dto / CabDTO.java
    jupiter / src / main / java / com / jupiter / application / web / rest / dto / CabDTOValidator.java
    jupiter / src / main / java / com / jupiter / application / service / CabService.java
    jupiter / src / main / java / com / jupiter / application / domain / Cab.java
    jupiter / src / main / java / com / jupiter / application / domain / Location.java
    jupiter / src / main / java / com / jupiter / application / repository / CabRepository.java

    <==TEST CODE==>
    jupiter / src / test / java / com / jupiter / application / service / CabServiceTest.java
    jupiter / src / test / java / com / jupiter / application / web / rest / CabsResourceTest.java
    jupiter / src / test / java / com / jupiter / application / data / DataLoader.java




    <==TEST DATA==>:
    100k lat long points near "Los Angeles" were chosen from geonames.org. 95% of the data is clean and rest
    had validation issues.

    100k seems to be approximate number of cabs in USA.
    (Source: http://en.wikipedia.org/wiki/Taxicabs_of_the_United_States)

    DB Collection stat:
    db.cab.stats()
    {
    	"ns" : "jupiter.cab",
    	"count" : 99570,
    	"size" : 23896800,   // collection size in bytes  23.8 MB
    	"avgObjSize" : 240,  // average object size in bytes
    	"storageSize" : 37797888,  // 37.8 MB
    	"numExtents" : 8,
    	"nindexes" : 2,
    	"lastExtentSize" : 15290368,
    	"paddingFactor" : 1,
    	"systemFlags" : 1,
    	"userFlags" : 1,
    	"totalIndexSize" : 6254640,   // total index size in bytes  6.25 MB
    	"indexSizes" : {                // size of specific indexes in bytes
    		"_id_" : 2796192,    // 2.8MB
    		"location_2dsphere" : 3458448 // 3.45 MB
    	},
    	"ok" : 1
    }

    <==Live Deployment:==>
    The project is deployed on Amazon EC2 t2.micro instance ( 1vCPU and 1GB RAM  *free tier limit*)
    on Microsoft 2008 server OS.
    


    <==Load Testing using JMeter (End-to-End):==>


    Load testing was performed using JMeter on both localhost and shared server.
    Up to 750 users were simulated to use Search API and results are as follows.


    On localhost:

            Search (time in milliseconds)

                    Users      Average time       90 percentile    Min    Max
                    200          2                      3           2       5
                    400          4                      6           2      17
                    600          5                     12           2      49
                    750          10                    27           1      128

    On Amazon EC2 Free Tier(Server in Oregon and Client in California):

            Search (time in milliseconds)

                    Users      Average time       90 percentile    Min    Max
                    200          248                  347          85       500
                    400          466                  870          90      1697
                    600          985                  1412         85      3247
                    750          2064                 3392         87      5724




    <==Future Work:==>


    1) Replace HTTP connections with WebSocket connections:
            Once the TCP connection is established, subsequent queries/ server-side push tasks will not have to setup
            a new connection unlike HTTP. This can save significant amount of time.

    2) Investigate if LMAX disruptor can leveraged to increase throughput.
            LMAX's relatively lock free design can increase throughput and help scale out resources.




