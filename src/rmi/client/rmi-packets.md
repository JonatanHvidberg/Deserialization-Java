# RMI packet analysis #

## RMI lookup ##
Packet:
```
0000   02 00 00 00 45 00 01 5a 00 00 40 00 40 06 00 00   ....E..Z..@.@...
0010   7f 00 00 01 7f 00 00 01 04 4b d7 9c 7a 97 47 6b   .........K..z.Gk
0020   f2 d2 3c e3 80 18 18 ea ff 4e 00 00 01 01 08 0a   ..<......N......
0030   9b f7 8e e5 c0 67 63 c7 51 ac ed 00 05 77 0f 01   .....gc.Q....w..
0040   92 0c 15 82 00 00 01 79 e8 7a b1 9f 80 0a 73 7d   .......y.z....s}
0050   00 00 00 01 00 12 72 6d 69 2e 63 6f 6d 6d 6f 6e   ......rmi.common
0060   2e 47 72 65 65 74 65 72 70 78 72 00 17 6a 61 76   .Greeterpxr..jav
0070   61 2e 6c 61 6e 67 2e 72 65 66 6c 65 63 74 2e 50   a.lang.reflect.P
0080   72 6f 78 79 e1 27 da 20 cc 10 43 cb 02 00 01 4c   roxy.'. ..C....L
0090   00 01 68 74 00 25 4c 6a 61 76 61 2f 6c 61 6e 67   ..ht.%Ljava/lang
00a0   2f 72 65 66 6c 65 63 74 2f 49 6e 76 6f 63 61 74   /reflect/Invocat
00b0   69 6f 6e 48 61 6e 64 6c 65 72 3b 70 78 70 73 72   ionHandler;pxpsr
00c0   00 2d 6a 61 76 61 2e 72 6d 69 2e 73 65 72 76 65   .-java.rmi.serve
00d0   72 2e 52 65 6d 6f 74 65 4f 62 6a 65 63 74 49 6e   r.RemoteObjectIn
00e0   76 6f 63 61 74 69 6f 6e 48 61 6e 64 6c 65 72 00   vocationHandler.
00f0   00 00 00 00 00 00 02 02 00 00 70 78 72 00 1c 6a   ..........pxr..j
0100   61 76 61 2e 72 6d 69 2e 73 65 72 76 65 72 2e 52   ava.rmi.server.R
0110   65 6d 6f 74 65 4f 62 6a 65 63 74 d3 61 b4 91 0c   emoteObject.a...
0120   61 33 1e 03 00 00 70 78 70 77 32 00 0a 55 6e 69   a3....pxpw2..Uni
0130   63 61 73 74 52 65 66 00 09 31 32 37 2e 30 2e 30   castRef..127.0.0
0140   2e 31 00 00 d6 de 48 8f ef 4e 13 0d 1e 6a 6b 1f   .1....H..N...jk.
0150   f8 d0 00 00 01 79 e8 7a c0 ff 80 01 01 78         .....y.z.....x
```
Break-down of packet data epilog:
```
00 0a 55 6e 69 63 61 73 74 52 65 66         "UnicastRef" (length: 10)
00 09 31 32 37 2e 30 2e 30 2e 31            "127.0.0.1"  (length: 9)
00 00 d6 de                                 port:        55006
48 8f ef 4e 13 0d 1e 6a                     objNum:      5228660811006549610
6b 1f f8 d0                                 uid.unique:  6b1ff8d0
00 00 01 79 e8 7a c0 ff                     uid.time:    179e87ac0ff
80 01                                       uid.count:   -7fff
01                                          isLocal:     true
78                                          end of object
```

## RMI call ##
Packet:
```
0000   02 00 00 00 45 00 00 a8 00 00 40 00 40 06 00 00   ....E.....@.@...
0010   7f 00 00 01 7f 00 00 01 e5 dc e4 74 8b 43 00 40   ...........t.C.@
0020   3c f7 c8 9c 80 18 18 e6 fe 9c 00 00 01 01 08 0a   <...............
0030   11 24 41 bd 18 83 f4 03 50 ac ed 00 05 77 22 3b   .$A.....P....w";
0040   40 3d ee 5c bb fd 7a e4 f9 af c7 00 00 01 79 e9   @=.\..z.......y.
0050   b3 45 fb 80 01 ff ff ff ff 0a 21 5d 96 fe fe bc   .E........!]....
0060   2b 73 72 00 12 72 6d 69 2e 63 6f 6d 6d 6f 6e 2e   +sr..rmi.common.
0070   52 65 71 75 65 73 74 a5 cf 47 db 57 c0 9a 22 02   Request..G.W..".
0080   00 01 4c 00 07 6d 65 73 73 61 67 65 74 00 12 4c   ..L..messaget..L
0090   6a 61 76 61 2f 6c 61 6e 67 2f 53 74 72 69 6e 67   java/lang/String
00a0   3b 70 78 70 74 00 05 77 6f 72 6c 64               ;pxpt..world
```
Break-down of packet data:
```
50                                          denotes a RMI call
ac ed                                       Java Object Serialization Protocol magic bytes
00 05                                       protocol version
77 22                                       block of data (34 bytes)
3b 40 3d ee 5c bb fd 7a                     objNum:      4269480540714564986
e4 f9 af c7                                 uid.unique:  -1b065039
00 00 01 79 e9 b3 45 fb                     uid.time:    179e9b345fb
80 01                                       uid.count:   -7fff
ff ff ff ff                                 <padding>
0a 21 5d 96 fe fe bc 2b                     method hash: 729967517715315755
73                                          new object
72                                          new class description
00 12 72 6d 69 2e 63 6f 6d 6d 6f 6e 2e 52
      65 71 75 65 73 74                     class name = "rmi.common.Request" (18 bytes)
a5 cf 47 db 57 c0 9a 22                     serialVersionUID
02                                          class desc flag SC_SERIALIZABLE
00 01                                       has 1 field
4c                                          object type code
00 07 6d 65 73 73 61 67 65                  field name = "message"  (7 bytes)
74                                          new string
00 12 4c 6a 61 76 61 2f 6c 61 6e 67 2f 53
      74 72 69 6e 67 3b                     field type = "Ljava/lang/String" (18 bytes)
70                                          class annotation = null
78                                          end of object
70                                          super class description = null
74                                          new string      (value of first field in first class)
00 05 77 6f 72 6c 64                        "world" (5 bytes)
```
