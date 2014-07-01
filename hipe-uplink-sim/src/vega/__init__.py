# 
#  This file is part of Herschel Common Science System (HCSS).
#  Copyright 2001-2010 Herschel Science Ground Segment Consortium
# 
#  HCSS is free software: you can redistribute it and/or modify
#  it under the terms of the GNU Lesser General Public License as
#  published by the Free Software Foundation, either version 3 of
#  the License, or (at your option) any later version.
# 
#  HCSS is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
#  GNU Lesser General Public License for more details.
# 
#  You should have received a copy of the GNU Lesser General
#  Public License along with HCSS.
#  If not, see <http://www.gnu.org/licenses/>.
# 
"""Herschel top-level namespace"""


def recursive_module_lookup(nameOfModule, pathToModule, fileToModule):
	"""Obsolete function"""
	#print nameOfModule, "{"
	#for item in pathToModule:
	#	print "    ",item
	#	pass
	#print "}", fileToModule
	#print
	return
# recursive_module_lookup end
#---

def loadpckg(name, prefix="herschel", pckgs=[]):
	"""
Routine for import importing the list of sub packages either using
"from prefix.pck.all import *"  or 
"from prefix.pck import *"
	"""
	import sys, traceback
	from java.util.logging import Logger
        all = "[No module named all]"
	MONITOR=Logger.getLogger("herschel.monitor");	
	MONITOR.fine( "scale:units=%s"%len(pckgs) )
	
	for pckg in pckgs:
		package = "%s.%s" % (prefix, pckg)
		MONITOR.fine("task:units=1")
		
	   	import_statement     = "from %s.all import *" % (package)
	   	alt_import_statement = "from %s     import *" % (package)
	   	message = "imported"
	   	try:
	   		exec import_statement in name
	   	except ImportError, e:
                        s = "[%s]" % (e)
                        if (not s[:len(all)] == all):
                           print "Import \"%s\" returns:" % (import_statement)
			   print "          Import Error -> %s" % (e)
	   		# try from pack import *
	   		try:
	   			exec alt_import_statement in name
	   		except ImportError, e:
	   			message = "skipped"
	   	except:
	   		print " Import Error -> %s" % import_statement
	   		print " Type: ", sys.exc_info()[0]   	   	
	   		print " Traceback: "
	   		traceback.print_tb(sys.exc_info()[2])

		MONITOR.fine("done:msg=%s %s"%(message,package))
	return
# loadpckg end
#---

# HCSS-6046: Backport 'enumerate' builtin function
class enumerate:
    def __init__(self, seq):
        self.seq = seq
        return
    def __getitem__(self, inx):
        return inx, self.seq[inx] 
    pass
#
import vega
#from rosetta.uplink.commanding import *
#from rosetta.uplink.pointing import *
#POR=SimulationContext.getInstance().sp
#PTR=SimulationContext.getInstance().ptr
# execute it here as well!
recursive_module_lookup(__name__,__path__,__file__)
