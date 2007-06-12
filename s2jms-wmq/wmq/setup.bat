REM === CREATE QUEUE MANAGER ===
crtmqm -q QMGR1

REM === START QUEUE MANAGER ===
strmqm QMGR1

REM === CREATE LOCAL QUEUES ===
runmqsc QMGR1 < QMGR1_StandAlone.def
