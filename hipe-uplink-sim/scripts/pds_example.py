from vega.hipe.pds import PDSImage
#This line open the PDS file and store the image into a variable called si
#You can download an example image from https://googledrive.com/host/0Bxc483hSRQ22NDdsTTdmMi1VNjQ/h0032_0000_bl3.img from Mars Express
si=PDSImage.readPdsFile('/your/path/h0032_0000_bl3.img')
#Now go to the Variables view, right click in si and select "Open With" and "PDS Navigator"
#In the navigator you can change the latitude and longitude of the upper corner of the view
#You can also click in the image a drag the mouse to measure a distance
#In the botton of the image you can change the scale of the image