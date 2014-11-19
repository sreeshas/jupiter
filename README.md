README for jupiter
==========================



Jupiter is a cab management solution which has the following features

    1) Search for the nearest cabs.
    2) Add/Update the location of a cab.
    3) Find a cab based on its Id.
    4) Delete a cab.

    Search:

    Current location of the user is determined by the browser and is automatically filled in. Nearby cabs around user
    location with radius of n meters is queried and displayed. User can increase the radius and it is reflected
    in the UI immediately.

    Search performs geo-spatial query using Mongodb which calculates distance based on spherical geometry.

    Add/Update

    Click anywhere on the map and an 'unsaved cab' icon is placed on the location clicked. User can enter the ID of the
    clicked cab and add it into the system. 'unsaved cab' will be saved and cab icon changes to reflect the same.

    Find

    User can find the location of any cab based on unique ID of the cab. if the cab is in the visible area of the map,
    searched cab is centered on the map with slight animation effect. In other positive cases, map is updated with cab
    in the center.

    Delete

    User can delete the cab from the system based on the ID. Updated map will be shown for successful delete.


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


    Technology Stack:

    Frontend: Angular JS
    Web Tier: Java and Spring MVC
    Backend: MongoDB

    The above stack is powered by JHipster which is really nice tool for quick prototypes.




