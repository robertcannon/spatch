package org.spatch.assembly.base;
 

public class ComponentPlug extends BasePlug {

   private PlugSpec p_spec;
   private AssemblyComponent p_component;
 

   public ComponentPlug(PlugSpec ps, AssemblyComponent cpt) {
      super(cpt, ps.getID());
      p_spec = ps;
      p_component = cpt;

      setRelativeAttachment(ps.getAttachmentPosition());

      setRelativePosition(p_spec.getInitialPosition());
    
   }



   public PlugSpec getSpec() {
      return p_spec;
   }


   public String getID() {
      return p_spec.getID();
   }


   public ConnectionFlavor getFlavor() {
      return p_spec.getConnectionFlavor();
   }


   public AssemblyComponent getComponent() {
      return p_component;
   }

  
 

 

}
