package nl.rug.sasleg.editor.gui;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.wfmc._2008.xpdl2.Activity;
import org.wfmc._2008.xpdl2.Artifact;
import org.wfmc._2008.xpdl2.Artifacts;
import org.wfmc._2008.xpdl2.ConstraintGroup;
import org.wfmc._2008.xpdl2.ConstraintGroups;
import org.wfmc._2008.xpdl2.Coordinates;
import org.wfmc._2008.xpdl2.Event;
import org.wfmc._2008.xpdl2.NodeGraphicsInfo;
import org.wfmc._2008.xpdl2.ObjectFactory;
import org.wfmc._2008.xpdl2.PackageType;
import org.wfmc._2008.xpdl2.ParallelConstraint;
import org.wfmc._2008.xpdl2.ProcessType;
import org.wfmc._2008.xpdl2.Route;
import org.wfmc._2008.xpdl2.Transition;
import org.wfmc._2008.xpdl2.WorkflowProcesses;
import org.wfmc._2008.xpdl2.XPDLVersion;

import nl.rug.sasleg.editor.model.ConstraintFlow;
import nl.rug.sasleg.editor.model.Element;
import nl.rug.sasleg.editor.model.EndEvent;
import nl.rug.sasleg.editor.model.Flow;
import nl.rug.sasleg.editor.model.FrozenGroup;
import nl.rug.sasleg.editor.model.Gate;
import nl.rug.sasleg.editor.model.Group;
import nl.rug.sasleg.editor.model.Node;
import nl.rug.sasleg.editor.model.NormalFlow;
import nl.rug.sasleg.editor.model.Parallel;
import nl.rug.sasleg.editor.model.Process;
import nl.rug.sasleg.editor.model.StartEvent;

public class XPDLMarshaller 
{
	private ObjectFactory of;
	private PackageType packageType;
	private ProcessType pt;
	
	public XPDLMarshaller(Process model)
	{
		of = new ObjectFactory();
		packageType = of.createPackageType();
		
		createProcessStructure(model);
	}
	
	private void createProcessStructure(Process model)
	{
		WorkflowProcesses wfps = of.createWorkflowProcesses();
		packageType.setWorkflowProcesses(wfps);
		
		pt = of.createProcessType();
		wfps.getWorkflowProcess().add(pt);
		
		createHeader();
		createGroups(model);
		createActivities(model);
		createTransitions(model);
	}
	
	private void createHeader()
	{
		packageType.setPackageHeader(of.createPackageHeader());
		
		XPDLVersion version = of.createXPDLVersion();
		version.setValue("2.1");
		packageType.getPackageHeader().setXPDLVersion(version);
	}
	
	private void createGroups(Process model)
	{
		ConstraintGroups cgs = of.createConstraintGroups();
		pt.setConstraintGroups(cgs);
		
		Artifacts artifacts = of.createArtifacts();
		packageType.setArtifacts(artifacts);
		
		for (Group group : model.getGroups())
		{
			if (group instanceof FrozenGroup)
			{
				ConstraintGroup cg = of.createConstraintGroup();
				
				cg.setId(group.getId());
				cg.setName(group.getName());
				createFrozenGroupGfx(group, cg);
				
				cgs.getConstraintGroup().add(cg);
			}
			else
			{
				Artifact artifact = of.createArtifact();
				artifact.setArtifactType("Group");
				org.wfmc._2008.xpdl2.Group xpdlGroup = of.createGroup();
				xpdlGroup.setId(group.getId());
				xpdlGroup.setName(group.getName());
				createGroupGfx(group, artifact);
				
				artifact.setGroup(xpdlGroup);
				artifacts.getArtifactAndAny().add(artifact);
			}
		}
	}
	
	private void createGroupGfx(Group group, Artifact artifact)
	{
		artifact.setNodeGraphicsInfos(of.createNodeGraphicsInfos());
		NodeGraphicsInfo gfxInfo = of.createNodeGraphicsInfo();
		
		gfxInfo.setWidth(group.getWidth());
		gfxInfo.setHeight(group.getHeight());
		
		Coordinates coords = of.createCoordinates();
		coords.setXCoordinate(group.getX());
		coords.setYCoordinate(group.getY());
		gfxInfo.setCoordinates(coords);
		
		artifact.getNodeGraphicsInfos().getNodeGraphicsInfo().add(gfxInfo);
	}
	
	private void createFrozenGroupGfx(Group group, ConstraintGroup xpdlCG)
	{
		xpdlCG.setNodeGraphicsInfos(of.createNodeGraphicsInfos());
		NodeGraphicsInfo gfxInfo = of.createNodeGraphicsInfo();
		
		gfxInfo.setWidth(group.getWidth());
		gfxInfo.setHeight(group.getHeight());
		
		Coordinates coords = of.createCoordinates();
		coords.setXCoordinate(group.getX());
		coords.setYCoordinate(group.getY());
		gfxInfo.setCoordinates(coords);
		
		xpdlCG.getNodeGraphicsInfos().getNodeGraphicsInfo().add(gfxInfo);
	}
	
	private void createActivities(Process model)
	{
		pt.setActivities(of.createActivities());
		
		for (Element element : model.getElements())
		{
			Activity activity = of.createActivity();
			
			activity.setId(element.getId());
			activity.setName(element.getName());
			
			createActivityType(element, activity);
			createActivityGfx(element, activity, model);
			
			pt.getActivities().getActivity().add(activity);
		}
	}
	
	private void createActivityType(Element modelElem, Activity xpdlActivity)
	{
		if (modelElem instanceof StartEvent)
		{
			Event event = of.createEvent();
			event.setStartEvent(of.createStartEvent());
			xpdlActivity.setEvent(event);
		}
		else if (modelElem instanceof EndEvent)
		{
			Event event = of.createEvent();
			event.setEndEvent(of.createEndEvent());
			xpdlActivity.setEvent(event);
		}
		else if (modelElem instanceof Gate)
		{
			Route route = of.createRoute();
			xpdlActivity.setRoute(route);
		}		
		
		if (modelElem != null)
		{
			xpdlActivity.setMandatory(modelElem.isMandatory());
			xpdlActivity.setFloating(modelElem.isFloating());
		}
	}
	
	private void createActivityGfx(Element modelElem, 
			Activity xpdlActivity, Process model)
	{
		xpdlActivity.setNodeGraphicsInfos(of.createNodeGraphicsInfos());
		NodeGraphicsInfo gfxInfo = of.createNodeGraphicsInfo();
		Coordinates coords = of.createCoordinates();
		
		coords.setXCoordinate(modelElem.getX());
		coords.setYCoordinate(modelElem.getY());
		gfxInfo.setCoordinates(coords);
		
		gfxInfo.setWidth(modelElem.getShape().getBounds2D().getWidth());
		gfxInfo.setHeight(modelElem.getShape().getBounds2D().getHeight());
		
		for (Group group : model.getGroups())
			if (group.hasElement(modelElem))
			{
				gfxInfo.setConstraintGroupId(group.getId());
				break;
			}
		
		xpdlActivity.getNodeGraphicsInfos().getNodeGraphicsInfo().add(gfxInfo);
	}
	
	private void createTransitions(Process model)
	{
		pt.setTransitions(of.createTransitions());
		
		for (Node element : model.getNodes())
		{
			for (Flow fl : element.getOutgoingFlows())
			{
				if (fl instanceof NormalFlow)
				{
					Transition transition = of.createTransition();
					
					transition.setFrom(fl.getSource().getId());
					transition.setTo(fl.getTarget().getId());
					transition.setId(fl.getId());
					transition.setName(fl.getName());
					transition.setWeaken(((NormalFlow)fl).isWeaken());
					
					pt.getTransitions().getTransition().add(transition);
				}
				else if (fl instanceof ConstraintFlow)
				{
					org.wfmc._2008.xpdl2.ConstraintFlow cf = 
											of.createConstraintFlow();
					
					cf.setFrom(fl.getSource().getId());
					cf.setTo(fl.getTarget().getId());
					cf.setId(fl.getId());
					cf.setName(fl.getName());
					
					cf.setNegation(((ConstraintFlow) fl).isNegated());
					cf.setPathQuantifier(
						((ConstraintFlow) fl).getPathQuantifier().toString());
					cf.setStateQuantifier(
						((ConstraintFlow) fl).getStateQuantifier().toString());
					
					pt.getTransitions().getConstraintFlow().add(cf);
				}
				else if (fl instanceof Parallel)
				{
					ParallelConstraint parallel = of.createParallelConstraint();
					
					parallel.setFrom(fl.getSource().getId());
					parallel.setTo(fl.getTarget().getId());
					parallel.setId(fl.getId());
					parallel.setName(fl.getName());
					
					pt.getTransitions().getParallelConstraint().add(parallel);
				}
			}			
		}
	}
		
	public void Marshall(File file)
	{
		try 
		{	
			pt.setAccessLevel("PUBLIC");
			pt.setName(file.getName());
			pt.setId(file.getName());
			
			JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
			Marshaller marshaller = context.createMarshaller();
			
			JAXBElement<PackageType> xmlRoot = of.createPackage(packageType);
			marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
			marshaller.marshal(xmlRoot, file);
			
		}
		catch (JAXBException e) 
		{
			e.printStackTrace();
		}
	}	
}
