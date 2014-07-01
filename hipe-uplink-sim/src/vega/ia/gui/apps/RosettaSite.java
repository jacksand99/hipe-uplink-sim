package vega.ia.gui.apps;

/*
 * This file is part of Herschel Common Science System (HCSS).
 * Copyright 2001-2010 Herschel Science Ground Segment Consortium
 *
 * HCSS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * HCSS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with HCSS.
 * If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * $Id: HipeSite.java,v 1.17 2013/07/15 16:13:06 jsaiz Exp $
 */

//package herschel.ia.gui.apps;

import herschel.ia.gui.apps.perspectives.WelcomePerspective;
import herschel.ia.gui.apps.perspectives.WorkBenchPerspective;
import herschel.ia.gui.apps.plugin.PluginRegistry;
import herschel.ia.gui.kernel.ComponentFactoryManager;
import herschel.ia.gui.kernel.Extension;
import herschel.ia.gui.kernel.ExtensionRegistry;
import herschel.ia.gui.kernel.FileSelection;
import herschel.ia.gui.kernel.FileSelectionFactory;
import herschel.ia.gui.kernel.StatusBarComponentFactory;
import herschel.ia.gui.kernel.ToolSelection;
import herschel.ia.gui.kernel.ToolSelectionFactory;
import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.gui.kernel.VariableSelectionFactory;
import herschel.ia.gui.kernel.parts.EditorArea;
import herschel.ia.gui.kernel.parts.EditorComponentFactory;
import herschel.ia.gui.kernel.parts.Site;
import herschel.ia.gui.kernel.parts.impl.AbstractSite;
import herschel.ia.gui.kernel.util.SiteUtil;
import herschel.share.interpreter.InterpreterNameSpaceUtil.Condition;
import herschel.share.swing.EDT;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * Hipe implementation for a site.
 */
//public class RosettaSite {
public class RosettaSite extends AbstractSite {
//public class RosettaSite extends herschel.ia.gui.apps.HipeSite {
	herschel.ia.gui.kernel.parts.impl.AbstractSite realSite;
    private static volatile boolean terminable = true;

    /**
     * Constructor.
     */
    public RosettaSite() {
    	this(true);
    	//init();
    	/*System.out.println("**************");
    	System.out.println(herschel.ia.gui.kernel.util.SiteUtil.getSite());
    	realSite = new herschel.ia.gui.apps.HipeSite(true);*/

    }
    
    /*public RosettaSite(boolean requiresGui) {
        super(requiresGui);
        PluginRegistry plugins = PluginRegistry.getInstance();
        final ClassLoader loader = plugins.getClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        if (requiresGui) {
            EDT.run(new Runnable() {
                @Override public void run() {
                    Thread.currentThread().setContextClassLoader(loader);
                }
            });
        }
    }*/
    public RosettaSite(boolean requiresGui) {
    	/*super(requiresGui);
    	Site site = herschel.ia.gui.kernel.util.SiteUtil.getSite();*/
    	//site.getComponentFactoryManager("site.statusbar")
    	//realSite = new herschel.ia.gui.apps.HipeSite(requiresGui);
        //super(requiresGui);
        //herschel.ia.gui.kernel.util.SiteUtil.setSite(this);
    	System.out.println("hello");
    	//realSite=new herschel.ia.gui.apps.HipeSite(requiresGui);
		String FACTORY = ExtensionRegistry.FACTORY;
		ExtensionRegistry REGISTRY = ExtensionRegistry.getInstance();

		REGISTRY.register(FACTORY, new Extension("site.resolver.factory","herschel.ia.gui.apps.HipeClassResolver","site.resolver.factory","herschel.ia.gui.apps.HipeClassResolver"));
		//REGISTRY.register(FACTORY, new Extension("javax.swing.JComponent","herschel.ia.gui.kernel.StatusBarComponentFactory","javax.swing.JComponentx","herschel.ia.gui.kernel.StatusBarComponentFactory"));
		REGISTRY.register(FACTORY, new Extension(FileSelectionFactory.ID,"herschel.ia.gui.kernel.FileSelectionFactory","herschel.ia.gui.kernel.FileSelection",EditorArea.ID));
		REGISTRY.register(FACTORY, new Extension(VariableSelectionFactory.ID,"herschel.ia.gui.kernel.VariableSelectionFactory","herschel.ia.gui.kernel.VariableSelection",EditorArea.ID));
		REGISTRY.register(FACTORY, new Extension(ToolSelectionFactory.ID,"herschel.ia.gui.kernel.ToolSelectionFactory","herschel.ia.gui.kernel.ToolSelection",EditorArea.ID));
		REGISTRY.register(FACTORY, new Extension(StatusBarComponentFactory.ID,"herschel.ia.gui.kernel.StatusBarComponentFactory","javax.swing.JComponent","site.statusbar"));
		REGISTRY.register(FACTORY, EditorComponentFactory.EXTENSION);
		//REGISTRY.getExtensions(REGISTRY.VIEWABLEPERSPECTIVECOMPONENT)
		
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.console","herschel.ia.jconsole.views.ConsoleView","Workbench/Console","herschel/ia/gui/kernel/icons/Console.gif"));
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.history","herschel.ia.jconsole.views.HistoryView","Workbench/History","herschel/ia/gui/kernel/icons/HistoryView.gif"));
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.log","herschel.ia.jconsole.views.LogView","Workbench/Log","herschel/ia/gui/kernel/icons/Log.gif"));
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.tasks","herschel.ia.task.views.TasksView","Workbench/Tasks","herschel/ia/gui/kernel/icons/TaskView.gif"));
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("herschel.ia.pal.browser.v2.hipe.ProductBrowserView","herschel.ia.pal.browser.v2.hipe.ProductBrowserView","Data Access/Product Browser","herschel/ia/gui/apps/views/welcome/contents/icons/access-pal_16px.png"));
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("herschel.ia.pal.browser.v2.hipe.ProductTreeView","herschel.ia.pal.browser.v2.hipe.ProductTreeView","Data Access/Product Tree","herschel/ia/gui/kernel/icons/OutlineView.gif"));
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.pal.save","herschel.ia.pal.gui.views.SaveProductsView","Data Access/Save Products to Pool","herschel/ia/gui/kernel/icons/Save.gif"));
		//REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.variables","herschel.ia.gui.apps.views.variables.VariablesView","Workbench/Variables","herschel/ia/gui/kernel/icons/VariablesView.gif"));
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.variables","vega.uplink.commanding.gui.SimulationContextView","Workbench/Variables","herschel/ia/gui/kernel/icons/VariablesView.gif"));

		
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.pal.export","herschel.ia.toolbox.util.views.ExportPalToUFDirView","Data Access/Export Herschel data from HIPE","herschel/ia/gui/kernel/icons/Export.gif"));
		//REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.hsa","herschel.ia.toolbox.hsa.views.HsaView","Herschel Science Archive","herschel/ia/gui/kernel/icons/ArchiveBrowserView.gif"));
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.plotxy.properties","herschel.ia.gui.plot.hipe.PlotPropertiesView","PlotXY properties","herschel/ia/gui/kernel/icons/Blank.gif"));
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.welcome","herschel.ia.gui.apps.views.welcome.WelcomeView","Welcome","herschel/ia/gui/kernel/icons/Help.gif"));
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.outline","herschel.ia.gui.apps.views.outline.OutlineView","Workbench/Outline","herschel/ia/gui/kernel/icons/OutlineView.gif"));
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.navigator","herschel.ia.gui.apps.views.navigator.NavigatorView","Workbench/Navigator","herschel/ia/gui/kernel/icons/NavigatorView.gif"));
		REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.simulation","vega.uplink.commanding.gui.SimulationView","Workbench/Simulation","herschel/ia/gui/kernel/icons/NavigatorView.gif"));
		
		//REGISTRY.register(REGISTRY.VIEWABLE,new Extension("pacs.cal.view.sets","herschel.pacs.cal.views.CalibrationSetsOverview","Workbench/Calibration Sets","herschel/pacs/cal/views/images/versions-16x16.png"));
		//REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.SpireCalView","herschel.spire.ia.cal.SpireCalView","Calibration/SPIRE Calibration","herschel/spire/ia/cal/images/versions.png"));
		//REGISTRY.register(REGISTRY.VIEWABLE,new Extension("site.view.calsdb_browser","herschel.calsdb.display.BrowserView","Reference/Calibrators","herschel/calsdb/resources/Saturn-icon.png"));
		
		REGISTRY.register(REGISTRY.PERSPECTIVE,new Extension("site.perspective.productbrowser","herschel.ia.pal.browser.v2.hipe.ProductBrowserPerspective","Product Browser","herschel/ia/gui/apps/views/welcome/contents/icons/access-pal_16px.png"));
		//REGISTRY.register(REGISTRY.PERSPECTIVE,new Extension("site.perspective.dataaccess","herschel.ia.toolbox.hsa.views.DataAccessPerspective","Data Access","herschel/ia/gui/apps/views/welcome/contents/icons/access-hsa_16px.png"));
		REGISTRY.register(REGISTRY.PERSPECTIVE,new Extension("site.perspective.welcome","herschel.ia.gui.apps.perspectives.WelcomePerspective","Welcome!","herschel/ia/gui/apps/views/welcome/contents/icons/overview_16px.png"));
		REGISTRY.register(REGISTRY.PERSPECTIVE,new Extension("site.perspective.workbench","herschel.ia.gui.apps.perspectives.WorkBenchPerspective","Workbench","herschel/ia/gui/apps/views/welcome/contents/icons/work-bench_16px.png"));
		
		REGISTRY.register(REGISTRY.FACTORY,new Extension("factory.editor.file","herschel.ia.gui.kernel.FileSelectionFactory","herschel.ia.gui.kernel.FileSelection","site.view.editor"));
		REGISTRY.register(REGISTRY.FACTORY,new Extension("factory.editor.variable","herschel.ia.gui.kernel.VariableSelectionFactory","herschel.ia.gui.kernel.VariableSelection","site.view.editor"));
		REGISTRY.register(REGISTRY.FACTORY,new Extension("factory.editor.tool","herschel.ia.gui.kernel.ToolSelectionFactory","herschel.ia.gui.kernel.ToolSelection","site.view.editor"));
		REGISTRY.register(REGISTRY.FACTORY,new Extension("factory.editor.new","herschel.ia.gui.kernel.parts.EditorComponentFactory","herschel.ia.gui.kernel.parts.EditorComponent","site.view.editor.new"));
		REGISTRY.register(REGISTRY.FACTORY,new Extension("factory.status","herschel.ia.gui.kernel.StatusBarComponentFactory","javax.swing.JComponent","site.statusbar"));
		REGISTRY.register(REGISTRY.FACTORY,new Extension("factory.editor.tool.task","herschel.ia.task.gui.dialog.TaskPanelFactory","herschel.ia.task.Task","site.view.editor"));
		REGISTRY.register(REGISTRY.FACTORY,new Extension("factory.editor.tool.task.signature","herschel.ia.task.gui.dialog.TaskSignatureComponentFactory","herschel.ia.task.Task","site.signature"));
		REGISTRY.register(REGISTRY.FACTORY,new Extension("site.resolver.factory","herschel.ia.gui.apps.HipeClassResolver","site.resolver.factory","herschel.ia.gui.apps.HipeClassResolver"));
		REGISTRY.register(REGISTRY.FACTORY,new Extension("factory.outline.variable","herschel.ia.gui.kernel.VariableSelectionFactory","herschel.ia.gui.kernel.VariableSelection","site.view.outline"));
		REGISTRY.register(REGISTRY.FACTORY,new Extension("factory.outline.file","herschel.ia.gui.kernel.FileSelectionFactory","herschel.ia.gui.kernel.FileSelection","site.view.outline"));
		REGISTRY.register(REGISTRY.FACTORY,new Extension("factory.outline.tool","herschel.ia.gui.kernel.ToolSelectionFactory","herschel.ia.gui.kernel.ToolSelection","site.view.outline"));
		REGISTRY.register(REGISTRY.FACTORY,new Extension("factory.modifier","herschel.ia.gui.apps.modifier.ModifierFactory","herschel.ia.gui.apps.modifier.Modifier","site.modifier"));
		
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Jython Script","herschel.ia.jconsole.views.JythonEditorComponent","factory.editor.new","herschel.ia.gui.kernel.FileSelection"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Text Document","herschel.ia.jconsole.views.TextEditorComponent","factory.editor.new","herschel.ia.gui.kernel.FileSelection"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Text Document","herschel.ia.jconsole.views.TextEditorComponent","factory.editor.file","herschel.ia.jconsole.views.TextFile,herschel.ia.gui.kernel.util.RegularFile"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Jython Script","herschel.ia.jconsole.views.JythonEditorComponent","factory.editor.file","herschel.ia.jconsole.views.JythonFile"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.dataset.gui.views.ArrayDataOutline","herschel.ia.dataset.gui.views.ArrayDataOutline","factory.outline.variable","herschel.ia.numeric.ArrayData"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.dataset.gui.views.CompositeDatasetOutline","herschel.ia.dataset.gui.views.CompositeDatasetOutline","factory.outline.variable","herschel.ia.dataset.CompositeDataset"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.dataset.gui.views.ProductOutline","herschel.ia.dataset.gui.views.ProductOutline","factory.outline.variable","herschel.ia.dataset.Product"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.dataset.gui.views.FitsFileOutline","herschel.ia.dataset.gui.views.FitsFileOutline","factory.outline.file","herschel.ia.dataset.gui.views.FitsFile"));

		
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("vega.uplink.commanding.gui.ModelStateOutline","vega.uplink.commanding.gui.ModelStateOutline","factory.outline.variable","vega.uplink.commanding.ModelState"));

		REGISTRY.register(REGISTRY.COMPONENT,new Extension("FITS Reader","herschel.ia.dataset.gui.views.FitsFileComponent","factory.editor.file","herschel.ia.dataset.gui.views.FitsFile"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ArrayData Viewer","herschel.ia.dataset.gui.views.ArrayDataComponent","factory.editor.variable","herschel.ia.numeric.ArrayData:1"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("HistoryModes Viewer","vega.uplink.commanding.gui.HistoryModesPlot","factory.editor.variable","vega.uplink.commanding.HistoryModes"));

		REGISTRY.register(REGISTRY.COMPONENT,new Extension("MetaData Viewer","herschel.ia.dataset.gui.views.MetaDataComponent","factory.editor.variable","herschel.ia.dataset.MetaData"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Dataset Viewer","herschel.ia.dataset.gui.views.DatasetComponent","factory.editor.variable","herschel.ia.dataset.Dataset:1,		  herschel.ia.dataset.ArrayDataset:1,		  herschel.ia.dataset.TableDataset:1,		  herschel.ia.dataset.CompositeDataset:1"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Product Viewer","herschel.ia.dataset.gui.views.ProductComponent","factory.editor.variable","herschel.ia.dataset.Product:1"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Task Dialog","herschel.ia.task.views.TaskToolComponent","factory.editor.tool","herschel.ia.task.views.TaskTool"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.task.views.TaskToolOutline","herschel.ia.task.views.TaskToolOutline","factory.outline.tool","herschel.ia.task.views.TaskTool"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Default Task Panel","herschel.ia.task.gui.dialog.JTaskPanel","factory.editor.tool.task","herschel.ia.task.Task"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Default Signature","herschel.ia.task.gui.dialog.JTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.task.Task"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.pal.browser.v2.views.StorageResultOutline","herschel.ia.pal.browser.v2.views.StorageResultOutline","factory.outline.variable","herschel.ia.pal.util.StorageResult"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ProductPool Outline","herschel.ia.pal.gui.components.ProductPoolOutline","factory.outline.variable","herschel.ia.pal.ProductPool"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.pal.gui.components.ProductRefOutline","herschel.ia.pal.gui.components.ProductRefOutline","factory.outline.variable","herschel.ia.pal.ProductRef"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.pal.gui.components.ContextOutline","herschel.ia.pal.gui.components.ContextOutline","factory.outline.variable","herschel.ia.pal.Context"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Context Viewer","herschel.ia.pal.gui.components.ContextComponent","factory.editor.variable","herschel.ia.pal.Context:1,herschel.ia.pal.ListContext:1,herschel.ia.pal.MapContext:1"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Save Products Tool","herschel.ia.pal.gui.views.SaveProductsEditorComponent","factory.editor.tool","herschel.ia.pal.gui.views.SaveProductsTool"));
		//REGISTRY.register(REGISTRY.COMPONENT,new Extension("Quality Viewer","herschel.ia.qcp.gui.QualityContextComponent","factory.editor.variable","herschel.ia.obs.quality.QualityContext"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.obs.gui.ObservationContextOutline","herschel.ia.obs.gui.ObservationContextOutline","factory.outline.variable","herschel.ia.obs.ObservationContext"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Observation Viewer","herschel.ia.obs.gui.ObservationContextComponent","factory.editor.variable","herschel.ia.obs.ObservationContext:1"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Browse Image Viewer","herschel.ia.obs.gui.BrowseImageComponent","factory.editor.variable","herschel.ia.obs.BrowseImageProduct"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Spectrum Explorer","herschel.ia.toolbox.spectrum.explorer.gui.views.SpectrumExplorerComponent","factory.editor.variable","herschel.ia.dataset.spectrum.SpectrumContainer:3"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Spectrum Explorer (SpectrumPlot)","herschel.ia.toolbox.spectrum.explorer.gui.views.SpectrumExplorerComponent","factory.editor.variable","herschel.ia.toolbox.spectrum.explorer.plot.SpectrumPlot:7"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Spectrum Panel","herschel.ia.toolbox.spectrum.explorer.panels.tool.SpectrumTaskPanel","factory.editor.tool.task","herschel.ia.toolbox.spectrum.SpectrumTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("SpectrumSignatureComponent","herschel.ia.toolbox.spectrum.explorer.panels.tool.component.SpectrumSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.spectrum.SpectrumTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("SmoothSpectrum Signature","herschel.ia.toolbox.spectrum.explorer.panels.tool.component.AccumulateSpectrumSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.spectrum.AccumulateSpectrumTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ArithmethicSpectrumTask Signature","herschel.ia.toolbox.spectrum.explorer.panels.tool.component.ArithmeticSpectrumSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.spectrum.ArithmeticSpectrumTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ResampleFrequency Signature","herschel.ia.toolbox.spectrum.explorer.panels.tool.component.ResampleSpectrumSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.spectrum.ResampleFrequencyTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("SmoothSpectrum Signature","herschel.ia.toolbox.spectrum.explorer.panels.tool.component.SmoothSpectrumSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.spectrum.SmoothSpectrumTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("SpectrumStatisticsTask Signature","herschel.ia.toolbox.spectrum.explorer.panels.tool.component.SpectrumStatisticsSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.spectrum.SpectrumStatisticsTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("StitchSpectrum Signature","herschel.ia.toolbox.spectrum.explorer.panels.tool.component.StitchSpectrumSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.spectrum.StitchSpectrumTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("FoldSpectrum Signature","herschel.ia.toolbox.spectrum.explorer.panels.tool.component.FoldSpectrumSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.spectrum.FoldSpectrumTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("SpectrumSignatureComponent","herschel.ia.toolbox.spectrum.explorer.panels.tool.component.FlagPixelsSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.spectrum.FlagPixelsTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ExtractSpectrumTask Signature","herschel.ia.toolbox.spectrum.explorer.panels.tool.component.ExtractSpectrumSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.spectrum.ExtractSpectrumTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("PairAverageSpectrum Signature","herschel.ia.toolbox.spectrum.explorer.panels.tool.component.PairAverageSpectrumSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.spectrum.PairAverageSpectrumTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Convert Wavescale Signature","herschel.ia.toolbox.spectrum.explorer.panels.tool.component.ConvertWaveScaleSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.spectrum.ConvertWavescaleTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ReplaceSpectrum Signature","herschel.ia.toolbox.spectrum.explorer.panels.tool.component.ReplaceFreqRangesSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.spectrum.ReplaceFreqRangesTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("SpectrumFitterGUI","herschel.ia.toolbox.spectrum.explorer.gui.views.SpectrumExplorerComponent","factory.editor.tool","herschel.ia.toolbox.spectrum.fit.gui.SpectrumFitterGUI"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("RotateTask Signature","herschel.ia.toolbox.image.gui.RotateTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.RotateTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ConvertImageUnitTask Signature","herschel.ia.toolbox.image.gui.ConvertUnitSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ConvertImageUnitTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("CutLevelsTask Signature","herschel.ia.toolbox.image.gui.CutLevelsTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.CutLevelsTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("TransposeTask Signature","herschel.ia.toolbox.image.gui.TransposeTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.TransposeTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("TranslateTask Signature","herschel.ia.toolbox.image.gui.TranslateTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.TranslateTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ScaleTask Signature","herschel.ia.toolbox.image.gui.ScaleTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ScaleTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ProfileTask Panel","herschel.ia.toolbox.image.gui.ProfilePanel","factory.editor.tool.task","herschel.ia.toolbox.image.ProfileTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("SourceFittingTask Panel","herschel.ia.toolbox.image.gui.SourceFittingPanel","factory.editor.tool.task","herschel.ia.toolbox.image.SourceFittingTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("AnnularSkyAperturePhotometry Panel","herschel.ia.toolbox.image.gui.AnnularSkyAperturePhotometryPanel","factory.editor.tool.task","herschel.ia.toolbox.image.AnnularSkyAperturePhotometryTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("RectangularSkyAperturePhotometry Panel","herschel.ia.toolbox.image.gui.RectangularSkyAperturePhotometryPanel","factory.editor.tool.task","herschel.ia.toolbox.image.RectangularSkyAperturePhotometryTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("FixedSkyAperturePhotometry Panel","herschel.ia.toolbox.image.gui.FixedSkyAperturePhotometryPanel","factory.editor.tool.task","herschel.ia.toolbox.image.FixedSkyAperturePhotometryTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageAddTask Signature","herschel.ia.toolbox.image.gui.ImageAddTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageAddTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageSubtractTask Signature","herschel.ia.toolbox.image.gui.ImageSubtractTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageSubtractTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageMultiplyTask Signature","herschel.ia.toolbox.image.gui.ImageMultiplyTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageMultiplyTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageDivideTask Signature","herschel.ia.toolbox.image.gui.ImageDivideTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageDivideTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageModuloTask Signature","herschel.ia.toolbox.image.gui.ImageModuloTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageModuloTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageAbsTask Signature","herschel.ia.task.gui.dialog.TaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageAbsTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageLogTask Signature","herschel.ia.task.gui.dialog.TaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageLogTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageLog10Task Signature","herschel.ia.task.gui.dialog.TaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageLog10Task"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageLogNTask Signature","herschel.ia.task.gui.dialog.TaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageLogNTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageExpTask Signature","herschel.ia.task.gui.dialog.TaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageExpTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageExp10Task Signature","herschel.ia.task.gui.dialog.TaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageExp10Task"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageExpNTask Signature","herschel.ia.task.gui.dialog.TaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageExpNTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageSquareTaskSignature","herschel.ia.task.gui.dialog.TaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageSquareTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImagePowerTaskSignature","herschel.ia.task.gui.dialog.TaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImagePowerTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageSqrtTask Signature","herschel.ia.task.gui.dialog.TaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageSqrtTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageRoundTask Signature","herschel.ia.task.gui.dialog.TaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageRoundTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageFloorTask Signature","herschel.ia.task.gui.dialog.TaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageFloorTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageCeilTask Signature","herschel.ia.task.gui.dialog.TaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageCeilTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ManualContourTask Panel","herschel.ia.toolbox.image.gui.ManualContourPanel","factory.editor.tool.task","herschel.ia.toolbox.image.ManualContourTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("AutomaticContourTask Signature","herschel.ia.toolbox.image.gui.AutomaticContourTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.AutomaticContourTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ImageHistogramTask Signature","herschel.ia.toolbox.image.gui.ImageHistogramTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ImageHistogramTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("CircleHistogramTask Panel","herschel.ia.toolbox.image.gui.CircleHistogramPanel","factory.editor.tool.task","herschel.ia.toolbox.image.CircleHistogramTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("EllipseHistogramTask Panel","herschel.ia.toolbox.image.gui.EllipseHistogramPanel","factory.editor.tool.task","herschel.ia.toolbox.image.EllipseHistogramTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("RectangleHistogramTask Panel","herschel.ia.toolbox.image.gui.RectangleHistogramPanel","factory.editor.tool.task","herschel.ia.toolbox.image.RectangleHistogramTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("PolygonHistogramTask Panel","herschel.ia.toolbox.image.gui.PolygonHistogramPanel","factory.editor.tool.task","herschel.ia.toolbox.image.PolygonHistogramTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ClampTask Signature","herschel.ia.toolbox.image.gui.ClampTaskSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.ClampTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("CreateRgbImageTask Signature","herschel.ia.toolbox.image.gui.CreateRgbImageComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.image.CreateRgbImageTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("CropTask Panel","herschel.ia.toolbox.image.gui.CropPanel","factory.editor.tool.task","herschel.ia.toolbox.image.CropTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Image Reader","herschel.ia.toolbox.image.gui.ImageFileComponent","factory.editor.file","herschel.ia.toolbox.image.gui.ImageFile"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Image Outline","herschel.ia.toolbox.image.gui.ImageFileOutline","factory.outline.file","herschel.ia.toolbox.image.gui.ImageFile"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Fits Reader Task Panel","herschel.ia.toolbox.util.gui.FileToOutputTaskPanel","factory.editor.tool.task","herschel.ia.toolbox.util.FitsReaderTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("AsciiTable Reader Task Panel","herschel.ia.toolbox.util.gui.FileToOutputTaskPanel","factory.editor.tool.task","herschel.ia.toolbox.util.AsciiTableReaderTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Restore Variables","herschel.ia.toolbox.util.gui.SaveFileComponent","factory.editor.file","herschel.ia.toolbox.util.gui.SaveFile"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("CSV Table Reader","herschel.ia.toolbox.util.gui.CsvFileComponent","factory.editor.file","herschel.ia.toolbox.util.gui.CsvFile"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Decompress File","herschel.ia.toolbox.util.gui.CompressedFileComponent","factory.editor.file","herschel.ia.toolbox.util.gui.CompressedFile"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Spaces Table Reader","herschel.ia.toolbox.util.gui.TblFileComponent","factory.editor.file","herschel.ia.toolbox.util.gui.TblFile"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Observation Reader","herschel.ia.toolbox.util.gui.ObservationXmlFileComponent","factory.editor.file","herschel.ia.toolbox.util.gui.ObservationXmlFile"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("ConvertUnitsTask signature","herschel.ia.toolbox.util.gui.ConvertUnitsSignatureComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.util.ConvertUnitsTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Power Spectrum Generator","herschel.ia.toolbox.astro.gui.PowerSpectrumComponent","factory.editor.variable","herschel.ia.dataset.TableDataset"));
		//REGISTRY.register(REGISTRY.COMPONENT,new Extension("HSA Status","herschel.ia.toolbox.hsa.HsaStatusLabel","factory.status","java.lang.Object"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("SourceExtractorTask Panel","herschel.ia.toolbox.srcext.gui.SrcExtPanel","factory.editor.tool.task","herschel.ia.toolbox.srcext.SourceExtractorTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("History Dataset Viewer","herschel.ia.toolbox.history.gui.HistoryDatasetViewer","factory.editor.variable","herschel.ia.dataset.history.HistoryDataset"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("PlotXY Viewer","herschel.ia.gui.plot.hipe.PlotEditorComponent","factory.editor.variable","herschel.ia.gui.plot.PlotXY"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("PlotXY Outline","herschel.ia.gui.plot.hipe.PlotOutlineComponent","factory.outline.variable","herschel.ia.gui.plot.PlotXY"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Annular sky aperture photometry explorer","herschel.ia.gui.image.AnnularSkyAperturePhotometryExplorer","factory.editor.variable","herschel.ia.dataset.image.AnnularSkyAperturePhotometryProduct"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Rectangular sky aperture photometry explorer","herschel.ia.gui.image.RectangularSkyAperturePhotometryExplorer","factory.editor.variable","herschel.ia.dataset.image.RectangularSkyAperturePhotometryProduct"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Fixed sky aperture photometry explorer","herschel.ia.gui.image.FixedSkyAperturePhotometryExplorer","factory.editor.variable","herschel.ia.dataset.image.FixedSkyAperturePhotometryProduct"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Image histogram explorer","herschel.ia.gui.image.ImageHistogramExplorer","factory.editor.variable","herschel.ia.dataset.image.ImageHistogramProduct"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Circle histogram explorer","herschel.ia.gui.image.CircleHistogramExplorer","factory.editor.variable","herschel.ia.dataset.image.CircleHistogramProduct"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Ellipse histogram explorer","herschel.ia.gui.image.EllipseHistogramExplorer","factory.editor.variable","herschel.ia.dataset.image.EllipseHistogramProduct"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Rectangle histogram explorer","herschel.ia.gui.image.RectangleHistogramExplorer","factory.editor.variable","herschel.ia.dataset.image.RectangleHistogramProduct"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Polygon histogram explorer","herschel.ia.gui.image.PolygonHistogramExplorer","factory.editor.variable","herschel.ia.dataset.image.PolygonHistogramProduct"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Profile explorer","herschel.ia.gui.image.ProfileExplorer","factory.editor.variable","herschel.ia.dataset.image.Profile"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Source fitting explorer","herschel.ia.gui.image.SourceFittingExplorer","factory.editor.variable","herschel.ia.dataset.image.SourceFittingProduct"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Wcs explorer for Images","herschel.ia.gui.image.WcsExplorer","factory.editor.variable","herschel.ia.dataset.image.Image"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Wcs explorer for Cubes","herschel.ia.gui.image.WcsExplorer","factory.editor.variable","herschel.ia.dataset.image.Cube"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Wcs explorer for RgbImage","herschel.ia.gui.image.WcsExplorer","factory.editor.variable","herschel.ia.dataset.image.RgbImage"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Wcs explorer","herschel.ia.gui.image.WcsExplorer","factory.editor.variable","herschel.ia.dataset.image.wcs.Wcs"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Standard Image Viewer","herschel.ia.gui.image.SimpleImageExplorer","factory.editor.variable","herschel.ia.dataset.image.Image"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Image Viewer for ArrayDatasets","herschel.ia.gui.image.SimpleImageExplorer","factory.editor.variable","herschel.ia.dataset.ArrayDataset"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("2d/3d Image Viewer","herschel.ia.gui.image.SimpleImageExplorer","factory.editor.variable","herschel.ia.numeric.ArrayData"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Standard color Image Viewer","herschel.ia.gui.image.SimpleImageExplorer","factory.editor.variable","herschel.ia.dataset.image.RgbImage"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Standard Cube Viewer","herschel.ia.gui.image.SimpleImageExplorer","factory.editor.variable","herschel.ia.dataset.image.Cube"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("List Viewer","herschel.ia.gui.apps.components.editor.ListEditorComponent","factory.editor.variable","java.util.List"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Map Viewer","herschel.ia.gui.apps.components.editor.MapEditorComponent","factory.editor.variable","java.util.Map"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("File Viewer","herschel.ia.gui.apps.components.editor.DefaultFileEditorComponent","factory.editor.file","herschel.ia.gui.kernel.util.RegularFile"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.ObjectOutline","herschel.ia.gui.apps.components.outline.ObjectOutline","factory.outline.variable","java.lang.Object"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.NumberOutline","herschel.ia.gui.apps.components.outline.NumberOutline","factory.outline.variable","java.lang.Number"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.NumberOutline","herschel.ia.gui.apps.components.outline.NumberOutline","factory.outline.variable","org.python.core.PyInteger"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.NumberOutline","herschel.ia.gui.apps.components.outline.NumberOutline","factory.outline.variable","org.python.core.PyLong"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.NumberOutline","herschel.ia.gui.apps.components.outline.NumberOutline","factory.outline.variable","org.python.core.PyFloat"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.NumberOutline","herschel.ia.gui.apps.components.outline.NumberOutline","factory.outline.variable","org.python.core.PyComplex"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.PyObjectOutline","herschel.ia.gui.apps.components.outline.PyObjectOutline","factory.outline.variable","org.python.core.PySequence"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.PyObjectOutline","herschel.ia.gui.apps.components.outline.PyObjectOutline","factory.outline.variable","org.python.core.PyDictionary"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.StringOutline","herschel.ia.gui.apps.components.outline.StringOutline","factory.outline.variable","org.python.core.PyString"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.StringOutline","herschel.ia.gui.apps.components.outline.StringOutline","factory.outline.variable","java.lang.String"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.MapOutline","herschel.ia.gui.apps.components.outline.MapOutline","factory.outline.variable","java.util.Map"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.CollectionOutline","herschel.ia.gui.apps.components.outline.CollectionOutline","factory.outline.variable","java.util.Collection"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.DefaultFileOutline","herschel.ia.gui.apps.components.outline.DefaultFileOutline","factory.outline.file","java.io.File"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("herschel.ia.gui.apps.components.outline.DefaultToolOutline","herschel.ia.gui.apps.components.outline.DefaultToolOutline","factory.outline.tool","herschel.ia.gui.kernel.Tool"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("DefaultByteModifier","herschel.ia.gui.apps.modifier.JByteModifier","factory.modifier","java.lang.Byte"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("DefaultShortModifier","herschel.ia.gui.apps.modifier.JShortModifier","factory.modifier","java.lang.Short"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("DefaultIntegerModifier","herschel.ia.gui.apps.modifier.JIntegerModifier","factory.modifier","java.lang.Integer"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("DefaultLongModifier","herschel.ia.gui.apps.modifier.JLongModifier","factory.modifier","java.lang.Long"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("DefaultFloatModifier","herschel.ia.gui.apps.modifier.JFloatModifier","factory.modifier","java.lang.Float"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("DefaultDoubleModifier","herschel.ia.gui.apps.modifier.JDoubleModifier","factory.modifier","java.lang.Double"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("DefaultStringModifier","herschel.ia.gui.apps.modifier.JStringModifier","factory.modifier","java.lang.String"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("DefaultBooleanModifier","herschel.ia.gui.apps.modifier.JBooleanModifier","factory.modifier","java.lang.Boolean"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("DefaultAngleModifier","herschel.ia.gui.apps.modifier.JAngleModifier","factory.modifier","herschel.ia.gui.apps.modifier.AngleValue"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Busy Job Progress Panel","herschel.ia.gui.apps.views.status.BusyJobProgressPanel","factory.status","java.lang.Object"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Separator 1","herschel.ia.gui.apps.views.status.Separator","factory.status","java.lang.Object"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("PositionDisplayer","herschel.ia.gui.apps.views.status.StatusPositionDisplayer","factory.status","java.lang.Object"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Separator 2","herschel.ia.gui.apps.views.status.Separator","factory.status","java.lang.Object"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Memory Bar","herschel.ia.gui.apps.views.status.MemoryBar","factory.status","java.lang.Object"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Free Needless Memory","herschel.ia.gui.apps.views.status.FreeMemoryButton","factory.status","java.lang.Object"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Separator 3","herschel.ia.gui.apps.views.status.Separator","factory.status","java.lang.Object"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Busy Job Icon","herschel.ia.gui.apps.views.status.BusyJobIcon","factory.status","java.lang.Object"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("TablePlotter","herschel.ia.gui.explorer.table.TablePlotterComponent","factory.editor.variable","herschel.ia.dataset.TableDataset"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("OverPlotter","herschel.ia.gui.explorer.table.OverPlotterComponent","factory.editor.variable","herschel.ia.dataset.TableDataset"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("computeVelocityMap component","herschel.ia.gui.cube.explorer.taskcomponents.ComputeVelocityMapTaskComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.cube.ComputeVelocityMapTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("computeVelocityMap component","herschel.ia.gui.cube.explorer.taskcomponents.CropCubeTaskComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.cube.CropCubeTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("extractRegionSpectrum component","herschel.ia.gui.cube.explorer.taskcomponents.ExtractRegionSpectrumTaskComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.cube.ExtractRegionSpectrumTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("integrateSpectralMap component","herschel.ia.gui.cube.explorer.taskcomponents.IntegrateSpectralMapTaskComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.cube.IntegrateSpectralMapTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("computePVMap component","herschel.ia.gui.cube.explorer.taskcomponents.ComputePVMapTaskComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.cube.ComputePVMapTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("subtractBaselineFromCube component","herschel.ia.gui.cube.explorer.taskcomponents.SubtractBaselineFromCubeTaskComponent","factory.editor.tool.task.signature","herschel.ia.toolbox.cube.SubtractBaselineFromCubeTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("VO Status","herschel.ia.vo.gui.VOStatus","factory.status","java.lang.Object"));
		/*REGISTRY.register(REGISTRY.COMPONENT,new Extension("pipeline signature","herschel.hifi.pipeline.HifiPipelineSignatureComponent","factory.editor.tool.task.signature","herschel.hifi.pipeline.HifiPipelineTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("HIFI Pipeline Task","herschel.hifi.pipeline.HifiObsComponent","factory.editor.variable","herschel.ia.obs.ObservationContext:2"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Summary Viewer","herschel.ia.dataset.gui.views.DatasetComponent","factory.editor.variable","herschel.hifi.pipeline.product.SummaryTable"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("pipeline configuration signature","herschel.hifi.pipeline.generic.utils.DoPipelineConfigurationSignature","factory.editor.tool.task.signature","herschel.hifi.pipeline.generic.DoPipelineConfigurationTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("pipeline level 2.5 signature","herschel.hifi.pipeline.generic.utils.Level2_5SignatureComponent","factory.editor.tool.task.signature","herschel.hifi.pipeline.generic.Level2_5PipelineTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("generic pipeline Task Gui","herschel.hifi.pipeline.util.gui.HifiPipelineSignatureGeneric","factory.editor.tool.task.signature","herschel.hifi.pipeline.generic.DoMainBeamTemperatureTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("generic pipeline Task Gui","herschel.hifi.pipeline.util.gui.HifiPipelineSignatureGeneric","factory.editor.tool.task.signature","herschel.hifi.pipeline.generic.DoFoldTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("generic pipeline Task Gui","herschel.hifi.pipeline.util.gui.HifiPipelineSignatureGeneric","factory.editor.tool.task.signature","herschel.hifi.pipeline.generic.MkRmsTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("generic pipeline Task Gui","herschel.hifi.pipeline.util.gui.HifiPipelineSignatureGeneric","factory.editor.tool.task.signature","herschel.hifi.pipeline.generic.MergeHtpsTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("DoGriddingTask Signature","herschel.hifi.dp.otf.gui.DoGriddingSignatureComponent","factory.editor.tool.task.signature","herschel.hifi.dp.otf.DoGriddingTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Spectrum Explorer (HifiProduct)","herschel.ia.toolbox.spectrum.explorer.gui.views.SpectrumExplorerComponent","factory.editor.variable","herschel.hifi.pipeline.product.HifiProduct"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("FitHifiFringeTask Signature","herschel.hifi.dp.standingwaves.FHFSignatureComponent","factory.editor.tool.task.signature","herschel.hifi.dp.standingwaves.FitHifiFringeTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("generic pipeline Task Gui","herschel.hifi.pipeline.util.gui.HifiPipelineSignatureGeneric","factory.editor.tool.task.signature","herschel.hifi.dp.tools.PolarPairTask"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("PACS Observation Summary Viewer","herschel.pacs.gui.common.PacsObsSummaryDatasetViewer","factory.editor.variable","herschel.pacs.signal.obs_summary.PacsObsSummaryDataset"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("PACS Spectral Footprint Viewer","herschel.pacs.gui.footprint.PacsSpectralFootprintViewer","factory.editor.tool","herschel.pacs.gui.footprint.PacsSpectralFootprintTool"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Pacs Product Viewer","herschel.pacs.gui.maskviewer.HipePacsProductDataExplorer","factory.editor.variable","herschel.pacs.signal.Frames, herschel.pacs.signal.Ramps, herschel.pacs.signal.ARamps, herschel.pacs.signal.TRamps, herschel.pacs.signal.PhotRaw"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Pacs Mask Array Viewer","herschel.pacs.gui.maskviewer.MaskArrayComponent","factory.editor.variable","herschel.pacs.signal.Mask, herschel.pacs.signal.AMask, herschel.pacs.signal.TMask, herschel.pacs.signal.CMask, herschel.ia.dataset.ArrayDataset"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Detector Timeline Viewer","herschel.spire.ia.gui.DetectorTimelineExplorer","factory.editor.variable","herschel.spire.ia.dataset.DetectorTimeline"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("SDI Explorer","herschel.spire.ia.gui.SpecExplorer","factory.editor.variable","herschel.spire.ia.dataset.SpectrometerDetectorInterferogram"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("SDS Explorer","herschel.spire.ia.gui.SpecExplorer","factory.editor.variable","herschel.spire.ia.dataset.SpectrometerDetectorSpectrum"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("STM Explorer","herschel.spire.ia.gui.SpecExplorer","factory.editor.variable","herschel.spire.ia.dataset.SpecTeleModel"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Level1 SpireMaskEditor","herschel.spire.ia.gui.SpireMaskEditorLevel1","factory.editor.variable","herschel.ia.obs.ObservationContext"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Level0_5 SpireMaskEditor","herschel.spire.ia.gui.SpireMaskEditorLevel0_5","factory.editor.variable","herschel.ia.obs.ObservationContext"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Spectrometer Level1 MaskEditor","herschel.spire.ia.gui.SpecLevel1MaskEditor","factory.editor.variable","herschel.ia.obs.ObservationContext"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Bolometer Finder Tool","herschel.spire.ia.tools.bolofinder.BoloFinderView","factory.editor.variable","herschel.ia.obs.ObservationContext"));
		REGISTRY.register(REGISTRY.COMPONENT,new Extension("Bolometer Finder","herschel.spire.ia.tools.bolofinder.BoloFinderView","factory.editor.tool","herschel.spire.ia.tools.bolofinder.BoloFinderTool"));*/
		//REGISTRY.register(REGISTRY.COMPONENT,new Extension("Calibrator Outline","herschel.calsdb.display.SourceOutline","factory.outline.variable","herschel.calsdb.display.model.SourceTransferObject"));
		//REGISTRY.register(REGISTRY.COMPONENT,new Extension("Calibrator Outline","herschel.calsdb.display.SourceOutline","factory.outline.variable","herschel.calsdb.model2.AbstractSource"));
		//REGISTRY.register(REGISTRY.COMPONENT,new Extension("Calibrator Viewer","herschel.calsdb.display.CalibratorViewer","factory.editor.variable","herschel.calsdb.model2.AbstractSource"));
		
		REGISTRY.register("UserPreferencesCategory",new Extension("General",null,null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("General/Appearance",null,null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("General/Appearance/Console","herschel.ia.jconsole.views.ConsolePreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers",null,null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Text Editor","herschel.ia.jconsole.views.TextEditorPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Text Editor/Jython Editor","herschel.ia.jconsole.views.JythonEditorPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("General/Help & Documentation","herschel.ia.document.yajahs.YajahsPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("General/Tasks","herschel.ia.task.views.TaskPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Data Access/Cache","herschel.ia.pal.pool.cache.gui.CachePrefsPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Data Access/Local Store","herschel.ia.pal.pool.lstore.prefs.LocalStorePreferencePanel",null,null));
		//REGISTRY.register("UserPreferencesCategory",new Extension("Data Access/My HSA","herschel.ia.pal.pool.hsa.gui.MyHSAPreferencePanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Data Access",null,null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Data Access/Storages & Pools","herschel.ia.pal.gui.prefs.StoragesAndPoolsPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Spectrum Explorer","herschel.ia.toolbox.spectrum.explorer.prefs.SpectrumPlotPrefPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Spectrum Explorer/WbsSpectrumDataset","herschel.ia.toolbox.spectrum.explorer.prefs.DatasetSpectrumPlotPrefPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Spectrum Explorer/HrsSpectrumDataset","herschel.ia.toolbox.spectrum.explorer.prefs.DatasetSpectrumPlotPrefPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Spectrum Explorer/SpectralSimpleCube","herschel.ia.toolbox.spectrum.explorer.prefs.DatasetSpectrumPlotPrefPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Spectrum Explorer/SpireSpectrum1d","herschel.ia.toolbox.spectrum.explorer.prefs.DatasetSpectrumPlotPrefPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Spectrum Explorer/SimpleSpectrum","herschel.ia.toolbox.spectrum.explorer.prefs.DatasetSpectrumPlotPrefPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Spectrum Explorer/SpectralLineList","herschel.ia.toolbox.spectrum.explorer.prefs.DatasetSpectrumPlotPrefPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Spectrum Explorer/Spectrum1d","herschel.ia.toolbox.spectrum.explorer.prefs.DatasetSpectrumPlotPrefPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Spectrum Explorer/Spectrum2d","herschel.ia.toolbox.spectrum.explorer.prefs.DatasetSpectrumPlotPrefPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/SpectrumFitterGUI","herschel.ia.toolbox.spectrum.fit.gui.userprefs.SFGPrefsPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("General/Directories","herschel.ia.gui.kernel.prefs.dir.DirectoryPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("General/Appearance/Fonts","herschel.ia.gui.kernel.prefs.font.FontPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/PlotXY Viewer","herschel.ia.gui.plot.hipe.prefs.PlotPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/PlotXY Viewer/Mouse","herschel.ia.gui.plot.hipe.prefs.mouse.PlotMousePreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Image Viewer","herschel.ia.gui.image.ImagePreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("General/Startup & Shutdown","herschel.ia.gui.apps.prefs.StartupPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("General/Startup & Shutdown/Import Files","herschel.ia.gui.apps.prefs.ImportsPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("General/Debug","herschel.ia.gui.apps.prefs.debug.DebugPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("General/Logging","herschel.ia.gui.apps.prefs.logging.LoggingPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("General/Navigator","herschel.ia.gui.apps.views.navigator.NavigatorPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/TablePlotter/Zoom & Pan Factors","herschel.ia.gui.explorer.table.ZoomPanFactorPreferencePanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/TablePlotter",null,null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("General/External Tools","herschel.ia.vo.prefs.ExtToolsPrefsPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Spectrum Explorer/HifiProduct","herschel.ia.toolbox.spectrum.explorer.prefs.DatasetSpectrumPlotPrefPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Spectrum Explorer/PacsCube","herschel.ia.toolbox.spectrum.explorer.prefs.DatasetSpectrumPlotPrefPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Spectrum Explorer/Frames","herschel.ia.toolbox.spectrum.explorer.prefs.DatasetSpectrumPlotPrefPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Data Access/Pacs Calibration","herschel.pacs.cal.prefs.PacsCalPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Data Access/SPIRE Calibration","herschel.spire.ia.cal.SpireCalPreferencesPanel",null,null));
		REGISTRY.register("UserPreferencesCategory",new Extension("Editors & Viewers/Detector Timeline Viewer","herschel.spire.ia.gui.DTEPreferencesPanel",null,null));
		
		
		
		REGISTRY.register("site.action.maker",new Extension("site.action.sendTo","herschel.ia.gui.kernel.menus.SendToActionMaker",null,null));
		REGISTRY.register("site.action.maker",new Extension("site.action.windows","herschel.ia.gui.kernel.menus.WindowsActionMaker",null,null));
		REGISTRY.register("site.action.maker",new Extension("site.action.prefs","herschel.ia.gui.kernel.prefs.PreferencesActionMaker",null,null));
		REGISTRY.register("site.action.maker",new Extension("site.action.scripts","herschel.ia.jconsole.views.ScriptsActionMaker",null,null));
		//REGISTRY.register("site.action.maker",new Extension("site.action.openhsaui","herschel.ia.toolbox.hsa.OpenHsaUiActionMaker",null,null));
		REGISTRY.register("site.action.maker",new Extension(null,"herschel.ia.gui.kernel.prefs.font.FontChangeActionMaker",null,null));
		REGISTRY.register("site.action.maker",new Extension("site.action.debug","herschel.ia.gui.apps.prefs.debug.DebugActionMaker",null,null));
		REGISTRY.register("site.action.maker",new Extension("site.action.help","herschel.ia.gui.apps.components.help.HelpActionMaker",null,null));
		REGISTRY.register("site.action.maker",new Extension("site.action.session","herschel.ia.gui.apps.components.session.SessionActionMaker",null,null));
		REGISTRY.register("site.action.maker",new Extension("site.action.tools","herschel.ia.gui.apps.components.tools.ToolsActionMaker",null,null));
		REGISTRY.register("site.action.maker",new Extension("site.action.stop","herschel.ia.gui.apps.views.status.StopActionMaker",null,null));
		REGISTRY.register("site.action.maker",new Extension("Interoperability actions","herschel.ia.vo.gui.InteropActionMaker",null,null));
		//REGISTRY.register("site.action.maker",new Extension("site.action.pipeline","herschel.hifi.pipeline.HifiActionMaker",null,null));
		//REGISTRY.register("site.action.maker",new Extension("site.help.dataReduction","herschel.hifi.pipeline.HifiHelpActionMaker",null,null));
		//REGISTRY.register("site.action.maker",new Extension("site.action.pacs-cal-update","herschel.pacs.cal.updater.UpdaterActionMaker",null,null));
		//REGISTRY.register("site.action.maker",new Extension("site.action.pipeline","herschel.pacs.init.PipelineActionMaker",null,null));
		//REGISTRY.register("site.action.maker",new Extension("site.action.scripts.pacs","herschel.pacs.init.PacsScriptsActionMaker",null,null));
		//REGISTRY.register("site.action.maker",new Extension("site.help.dataReduction.pacs","herschel.pacs.init.PacsHelpActionMaker",null,null));
		//REGISTRY.register("site.action.maker",new Extension("site.action.spire-cal-update","herschel.spire.ia.cal.SpireCalUpdaterActionMaker",null,null));
		//REGISTRY.register("site.action.maker",new Extension("site.action.pipeline","herschel.spire.ia.pipeline.hipe.SpireActionMaker",null,null));
		REGISTRY.register("site.action.maker",new Extension("site.help","herschel.spire.ia.pipeline.hipe.SpireHelpActionMaker",null,null));
		
		
		REGISTRY.register("site.fileType",new Extension("site.view.navigator.jython","herschel.ia.jconsole.views.JythonFile","py","herschel/ia/gui/kernel/icons/Python.gif"));
		REGISTRY.register("site.fileType",new Extension("site.view.navigator.text","herschel.ia.jconsole.views.TextFile","txt,log,dat,bat,config,props,properties,java,html,xml,sh,itl,ROS","herschel/ia/gui/kernel/icons/TextFile.gif"));
		REGISTRY.register("site.fileType",new Extension("site.view.navigator.fits","herschel.ia.dataset.gui.views.FitsFile","fits,fit,fts,fits.gz,fit.gz,fts.gz,fits.gzip,fit.gzip,fts.gzip,fits.zip,fit.zip,fts.zip","herschel/ia/gui/kernel/icons/data/Fits.gif"));
		REGISTRY.register("site.fileType",new Extension("site.file.image","herschel.ia.toolbox.image.gui.ImageFile","jpg,jpeg,png,gif,tif,tiff,bmp","herschel/ia/gui/kernel/icons/data/Image.gif"));
		REGISTRY.register("site.fileType",new Extension("site.view.navigator.save","herschel.ia.toolbox.util.gui.SaveFile","ser","herschel/ia/gui/kernel/icons/Variable.gif"));
		REGISTRY.register("site.fileType",new Extension("site.view.navigator.csv","herschel.ia.toolbox.util.gui.CsvFile","csv","herschel/ia/gui/kernel/icons/data/Csv.gif"));
		REGISTRY.register("site.fileType",new Extension("site.view.navigator.compressed","herschel.ia.toolbox.util.gui.CompressedFile","zip,gzip,gz,tar,tgz","herschel/ia/gui/kernel/icons/Compressed.gif"));
		REGISTRY.register("site.fileType",new Extension("site.view.navigator.tbl","herschel.ia.toolbox.util.gui.TblFile","tbl","herschel/ia/gui/kernel/icons/data/Csv.gif"));
		//REGISTRY.register("site.fileType",new Extension("site.view.navigator.jython","herschel.ia.toolbox.util.gui.ObservationXmlFile","regex:^[\d]+-herschel.ia.obs.ObservationContext-[\d]+[.]xml$","herschel/ia/pal/pool/hsa/obsid.gif"));
		
		REGISTRY.register("site.help.manager",new Extension(null,"herschel.ia.document.HelpManager",null,null));
		//herschel.ia.gui.kernel.prefs.UserPreferences.importFrom(new java.io.File("Z:\\MAPPS\\user.pref"));
		String PERSPECTIVE = ExtensionRegistry.PERSPECTIVE;
				//REGISTRY    = ExtensionRegistry.getInstance()

		REGISTRY.register(PERSPECTIVE, new Extension(WelcomePerspective.ID,"herschel.ia.gui.apps.perspectives.WelcomePerspective",WelcomePerspective.TITLE,"herschel/ia/gui/apps/views/welcome/contents/icons/overview_16px.png"));

		REGISTRY.register(PERSPECTIVE, new Extension(WorkBenchPerspective.ID,"herschel.ia.gui.apps.perspectives.WorkBenchPerspective",WorkBenchPerspective.TITLE,"herschel/ia/gui/apps/views/welcome/contents/icons/work-bench_16px.png"));
		
		System.out.println("Default perspective:");
		//System.out.println(this.getPerspectiveManager().);
		
		System.out.println(this.getPerspectiveManager().getDefault());
		PluginRegistry plugins = PluginRegistry.getInstance();
        final ClassLoader loader = plugins.getClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        if (requiresGui) {
            EDT.run(new Runnable() {
                @Override public void run() {
                    Thread.currentThread().setContextClassLoader(loader);
                }
            });
        }
        if (herschel.ia.gui.kernel.util.SiteUtil.getSite()==null) herschel.ia.gui.kernel.util.SiteUtil.setSite(this);
        Site site = SiteUtil.getSite();
        if (site != null) {
        	System.out.println("site is not null");
            ComponentFactoryManager manager = site.getComponentFactoryManager("site.statusbar");
            StatusBarComponentFactory instance = (StatusBarComponentFactory)manager.getComponentFactory(JComponent.class.getName());
            herschel.ia.gui.kernel.SimpleComponentFactoryManager man = new herschel.ia.gui.kernel.SimpleComponentFactoryManager(ExtensionRegistry.FACTORY);
            instance = (StatusBarComponentFactory)man.getComponentFactory(JComponent.class.getName());
            System.out.println(manager);
            if (instance==null) System.out.println("instance is null");
        }
        
    }

    @Override
    public String getTitle() {
        return "RSP - Rosetta Interactive Processing Environment";
    }

    @Override
    public String getName() {
        return "RSP";
    }

    @Override
    public String getProject() {
        return "RSP";
    }

    @Override
    public Image getLogo() {
        return new ImageIcon(getClass().getResource("logo.jpg")).getImage();
    }

    @Override
    public URL getSplashImage() {
        return getClass().getResource("splash.jpg");
    }

    @Override
    public void init() {
        PluginRegistry.getInstance().startPlugins();
        super.init();
    }

    @Override
    public boolean isTerminable() {
        return terminable;
    }

    // For testing
    static void setTerminable(boolean terminable) {
        RosettaSite.terminable = terminable;
    }
}


