import {http_get} from '../api/API'
import Template from "./Template";

export async function getTemplateList() {
    const templates = await http_get("templates");
    return templates.map(t => new Template(t));
}

// requestAck must be transformed in requiresAck
export async function getTemplateById(templateId, messageId, correlationId, requiresAck) {
    console.log("getTemplateById");
    const template = await http_get(`templates/${templateId}`,
        {
            messageId: messageId,
            correlationId: correlationId,
            requestAck: requiresAck
        }
    );
    

    if(template.errorCode){
        // return Error object
        console.log("getTemplateById retuned with an error: ", template);
        return template;
    }

    console.log("getTemplateById: ", template);
    return new Template(template);

}