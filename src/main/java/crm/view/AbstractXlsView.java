package crm.view;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.AbstractView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;

/**
 * Abstract base class for Excel document views.
 * This class replaces the deprecated AbstractXlsView from Spring Framework.
 */
public abstract class AbstractXlsView extends AbstractView {

    /**
     * Default Constructor. Sets the content type to "application/vnd.ms-excel".
     */
    public AbstractXlsView() {
        setContentType("application/vnd.ms-excel");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception {
        // Create the Excel workbook
        Workbook workbook = createWorkbook(model, request);

        // Set the content type
        response.setContentType(getContentType());

        // Build the Excel document
        buildExcelDocument(model, workbook, request, response);

        // Write to response output stream
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.flush();
    }

    /**
     * Creates the workbook instance. Can be overridden to create specific workbook types.
     * Default implementation creates an HSSFWorkbook (XLS format).
     *
     * @param model   the model Map
     * @param request the current HTTP request
     * @return the created Workbook instance
     * @throws Exception if workbook creation fails
     */
    protected Workbook createWorkbook(Map<String, Object> model, HttpServletRequest request) throws Exception {
        return new org.apache.poi.hssf.usermodel.HSSFWorkbook();
    }

    /**
     * Subclasses must implement this method to populate the Excel workbook.
     *
     * @param model    the model Map
     * @param workbook the Excel workbook to populate
     * @param request  the current HTTP request
     * @param response the current HTTP response
     * @throws Exception if document building fails
     */
    protected abstract void buildExcelDocument(Map<String, Object> model,
                                                Workbook workbook,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception;
}
