
package acme.forms.agent;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentDashboard extends AbstractForm {

	private static final long		serialVersionUID	= 1L;

	private Double					resolvedClaimRatio;
	private Double					rejectedClaimRatio;
	private List<String>			topThreeMonthsWithMostClaims;
	private List<LogStatistics>		logStatistics;
	private List<ClaimStatistics>	claimStatistics;
}
