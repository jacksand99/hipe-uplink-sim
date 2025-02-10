#
# Create synthetic parameters: example for C100 sensors
#

# T112
HK = ["KM205303","KM215301"] 
startDate = "2009-05-14 3:22:39"
endDate = "2009-05-17 3:22:39"

prodRaw = mustReader(HK,startDate,endDate) 


cp = PlotXY()
for pars in range(len(HK)) :
        data=prodRaw[HK[pars]][HK[pars]].data
        time=prodRaw[HK[pars]]["Time"].data
        p1=LayerXY(data,time)
        p1.setName(HK[pars])
        cp.setLayer(pars,p1,0,0)  

cp.yaxis.title.text="Time"
cp.xaxis.title.text="Temperature [K]"
cp.getLegend().setVisible(True)
store=ProductStorage()
store.register(LocalStoreFactory.getStore("must"))
store.save(prodRaw)