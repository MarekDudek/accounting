package md.accounting.ipko.domain;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.xml.bind.annotation.XmlAccessType.FIELD;

@XmlRootElement(name = "account-history")
@XmlAccessorType(FIELD)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountHistory
{
    @XmlElement(required = true)
    private Search search;

    @XmlElement(required = true)
    private Operations operations;
}
