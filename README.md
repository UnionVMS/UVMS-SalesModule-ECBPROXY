# SalesModule-PROXY-ECB

See https://focusfish.atlassian.net/wiki/display/UVMS/Sales for documentation


## Server configuration
### Queue
In subsystem "resource-adapters", between <admin-objects>:

```xml
<admin-object class-name="org.apache.activemq.command.ActiveMQQueue" jndi-name="java:/jms/queue/UVMSSalesEcbProxy" use-java-context="true" pool-name="UVMSSalesEcbProxy">
   <config-property name="PhysicalName">
       UVMSSalesEcbProxy
   </config-property>
</admin-object>
```                               
## Related Repositories

* https://github.com/UnionVMS/UVMS-SalesModule-MODEL
* https://github.com/UnionVMS/UVMS-SalesModule-DB
* https://github.com/UnionVMS/UVMS-SalesModule-APP